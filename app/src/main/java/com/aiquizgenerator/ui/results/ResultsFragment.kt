package com.aiquizgenerator.ui.results
import android.os.Bundle; import android.view.LayoutInflater; import android.view.View; import android.view.ViewGroup
import androidx.fragment.app.Fragment; import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiquizgenerator.R; import com.aiquizgenerator.data.remote.model.QuizResult
import com.aiquizgenerator.databinding.FragmentResultsBinding; import com.google.gson.Gson; import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsFragment : Fragment() {
    private var _b: FragmentResultsBinding? = null; private val b get() = _b!!
    private val resultJson by lazy { arguments?.getString("resultJson") ?: "" }
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentResultsBinding.inflate(i,c,false).also{_b=it}.root
    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v,s)
        if (resultJson.isNotEmpty()) { val r = Gson().fromJson(resultJson, QuizResult::class.java); display(r) }
    }
    private fun display(r: QuizResult) {
        b.tvScorePercent.text="${r.score}%"; b.progressCircle.progress=r.score
        b.tvTotalQuestions.text="${r.totalQuestions}"; b.tvCorrectAnswers.text="${r.correctAnswers}"
        b.tvWrongAnswers.text="${r.wrongAnswers}"; b.tvUnattempted.text="${r.unattempted}"
        b.tvGradeMessage.text=when{ r.score>=90->"🏆 Excellent!"; r.score>=75->"🎉 Great job!"; r.score>=60->"👍 Good effort!"; r.score>=40->"📚 Keep studying!"; else->"💪 Don't give up!" }
        b.rvAnswerReview.layoutManager=LinearLayoutManager(requireContext()); b.rvAnswerReview.adapter=AnswerReviewAdapter(r.questions)
        b.btnRetakeQuiz.setOnClickListener { findNavController().popBackStack(R.id.uploadFragment,false) }
    }
    override fun onDestroyView(){super.onDestroyView();_b=null}
}
