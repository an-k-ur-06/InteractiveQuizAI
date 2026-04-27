package com.aiquizgenerator.repository
import com.aiquizgenerator.data.local.dao.QuizQuestionDao
import com.aiquizgenerator.data.local.dao.QuizSessionDao
import com.aiquizgenerator.data.local.entity.QuizQuestionEntity
import com.aiquizgenerator.data.local.entity.QuizSessionEntity
import com.aiquizgenerator.data.remote.api.GeminiApiService
import com.aiquizgenerator.data.remote.model.*
import com.aiquizgenerator.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizRepository @Inject constructor(
    private val api: GeminiApiService,
    private val sessionDao: QuizSessionDao,
    private val questionDao: QuizQuestionDao,
    private val apiKey: String
) {
    suspend fun generateQuiz(config: QuizConfig): Resource<List<QuizQuestion>> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
Generate ${config.numberOfQuestions} multiple choice questions from the study material below at ${config.difficulty.label} difficulty.
Return ONLY valid JSON, no markdown, no explanation:
{"questions":[{"question":"...","options":["A","B","C","D"],"correct_answer":"A"}]}

Study Material:
${config.extractedText.take(12000)}
""".trimIndent()
            val resp = api.generateContent(apiKey, GeminiRequest(listOf(Content(listOf(Part(prompt))))))
            if (!resp.isSuccessful) return@withContext Resource.Error("API error ${resp.code()}")
            val text = resp.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: return@withContext Resource.Error("Empty response from Gemini")
            val questions = parseQuestions(text)
            if (questions.isEmpty()) Resource.Error("Could not parse questions — check your API key and study material")
            else Resource.Success(questions)
        } catch (e: Exception) { Resource.Error(e.localizedMessage ?: "Network error") }
    }

    private fun parseQuestions(raw: String): List<QuizQuestion> = try {
        val clean = raw.replace("```json","").replace("```","").trim()
        val arr = JSONObject(clean).getJSONArray("questions")
        (0 until arr.length()).map { i ->
            val q = arr.getJSONObject(i)
            val opts = (0 until q.getJSONArray("options").length()).map { q.getJSONArray("options").getString(it) }
            QuizQuestion(q.getString("question"), opts, q.getString("correct_answer"))
        }
    } catch (e: Exception) { emptyList() }

    suspend fun saveSession(s: QuizSessionEntity) = sessionDao.insert(s)
    suspend fun updateSession(s: QuizSessionEntity) = sessionDao.update(s)
    suspend fun saveQuestions(q: List<QuizQuestionEntity>) = questionDao.insertAll(q)
    fun allSessions() = sessionDao.getAll()
}
