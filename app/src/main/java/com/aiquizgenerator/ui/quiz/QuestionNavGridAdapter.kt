package com.aiquizgenerator.ui.quiz
import android.view.LayoutInflater; import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aiquizgenerator.R
import com.aiquizgenerator.data.remote.model.QuizQuestion
import com.aiquizgenerator.databinding.ItemQuestionNavBinding

class QuestionNavGridAdapter(private val questions: List<QuizQuestion>, private val onClick: (Int) -> Unit) : RecyclerView.Adapter<QuestionNavGridAdapter.VH>() {
    inner class VH(val b: ItemQuestionNavBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(ItemQuestionNavBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun getItemCount() = questions.size
    override fun onBindViewHolder(h: VH, pos: Int) {
        val q = questions[pos]; val ctx = h.itemView.context
        h.b.tvNumber.text = "${pos+1}"
        h.b.root.backgroundTintList = ctx.getColorStateList(when { q.isMarkedForReview -> R.color.color_marked; q.isAnswered -> R.color.color_answered; else -> R.color.color_unanswered })
        h.b.root.setOnClickListener { onClick(pos) }
    }
}
