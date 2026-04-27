package com.aiquizgenerator.ui.results
import android.view.LayoutInflater; import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aiquizgenerator.R
import com.aiquizgenerator.data.remote.model.QuizQuestion
import com.aiquizgenerator.databinding.ItemAnswerReviewBinding

class AnswerReviewAdapter(private val questions: List<QuizQuestion>) : RecyclerView.Adapter<AnswerReviewAdapter.VH>() {
    inner class VH(val b: ItemAnswerReviewBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(ItemAnswerReviewBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun getItemCount() = questions.size
    override fun onBindViewHolder(h: VH, pos: Int) {
        val q = questions[pos]; val ctx = h.itemView.context; val b = h.b
        b.tvQuestionNumber.text = "Q${pos+1}"; b.tvQuestion.text = q.question
        b.tvCorrectAnswer.text = "✓ Correct: ${q.correctAnswer}"
        when {
            q.userAnswer == null -> { b.tvUserAnswer.text = "— Not attempted"; b.tvUserAnswer.setTextColor(ctx.getColor(R.color.color_unanswered_text)); b.cardRoot.strokeColor=ctx.getColor(R.color.color_unanswered_text) }
            q.userAnswer == q.correctAnswer -> { b.tvUserAnswer.text = "✓ ${q.userAnswer}"; b.tvUserAnswer.setTextColor(ctx.getColor(R.color.color_success)); b.cardRoot.strokeColor=ctx.getColor(R.color.color_success) }
            else -> { b.tvUserAnswer.text = "✗ ${q.userAnswer}"; b.tvUserAnswer.setTextColor(ctx.getColor(R.color.color_error)); b.cardRoot.strokeColor=ctx.getColor(R.color.color_error) }
        }
    }
}
