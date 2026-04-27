package com.aiquizgenerator.ui.quiz
import androidx.lifecycle.*
import com.aiquizgenerator.data.local.entity.QuizQuestionEntity
import com.aiquizgenerator.data.local.entity.QuizSessionEntity
import com.aiquizgenerator.data.remote.model.QuizQuestion
import com.aiquizgenerator.data.remote.model.QuizResult
import com.aiquizgenerator.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(private val repo: QuizRepository) : ViewModel() {
    private val _idx = MutableLiveData(0); val currentIndex: LiveData<Int> = _idx
    private val _timer = MutableLiveData(0); val timerSeconds: LiveData<Int> = _timer
    private val _submitted = MutableLiveData(false); val quizSubmitted: LiveData<Boolean> = _submitted
    private val _result = MutableLiveData<QuizResult>(); val quizResult: LiveData<QuizResult> = _result
    val questions = mutableListOf<QuizQuestion>()
    private var timerJob: Job? = null
    private val sessionId = UUID.randomUUID().toString()

    fun initialize(qs: List<QuizQuestion>, minutes: Int, fileName: String, difficulty: String) {
        questions.clear(); questions.addAll(qs); _timer.value = minutes * 60
        viewModelScope.launch {
            repo.saveSession(QuizSessionEntity(sessionId, fileName, difficulty, qs.size, minutes))
            repo.saveQuestions(qs.mapIndexed { i, q -> QuizQuestionEntity(0, sessionId, q.question, q.options, q.correctAnswer, null, i) })
        }
        timerJob?.cancel(); timerJob = viewModelScope.launch {
            var t = minutes * 60
            while (t > 0) { delay(1000); t--; _timer.value = t }
            submitQuiz()
        }
    }

    fun goToQuestion(i: Int) { if (i in questions.indices) _idx.value = i }
    fun goNext() { val n = (_idx.value ?: 0) + 1; if (n < questions.size) _idx.value = n }
    fun goPrevious() { val p = (_idx.value ?: 0) - 1; if (p >= 0) _idx.value = p }
    fun selectAnswer(i: Int, ans: String) { questions[i].userAnswer = ans; questions[i].isAnswered = true; _idx.value = _idx.value }
    fun toggleMarkForReview(i: Int) { questions[i].isMarkedForReview = !questions[i].isMarkedForReview; _idx.value = _idx.value }
    fun saveAndNext(i: Int, ans: String?) { ans?.let { selectAnswer(i, it) }; goNext() }

    fun submitQuiz() {
        timerJob?.cancel()
        val correct = questions.count { it.userAnswer == it.correctAnswer }
        val wrong = questions.count { it.userAnswer != null && it.userAnswer != it.correctAnswer }
        val unattempted = questions.count { it.userAnswer == null }
        val score = if (questions.isNotEmpty()) correct * 100 / questions.size else 0
        _result.value = QuizResult(questions.size, correct, wrong, unattempted, score, questions.toList())
        viewModelScope.launch { repo.updateSession(QuizSessionEntity(sessionId, "", "", questions.size, 0, score, correct, wrong, unattempted, System.currentTimeMillis(), true)) }
        _submitted.value = true
    }

    fun getAnsweredCount() = questions.count { it.isAnswered }
    fun getMarkedCount() = questions.count { it.isMarkedForReview }
    fun getUnansweredCount() = questions.count { !it.isAnswered }
    override fun onCleared() { super.onCleared(); timerJob?.cancel() }
}
