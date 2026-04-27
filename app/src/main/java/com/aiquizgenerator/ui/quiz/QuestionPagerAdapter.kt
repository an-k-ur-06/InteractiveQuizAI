package com.aiquizgenerator.ui.quiz
import android.view.LayoutInflater; import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aiquizgenerator.R
import com.aiquizgenerator.data.remote.model.QuizQuestion
import com.aiquizgenerator.databinding.ItemQuestionPageBinding

class QuestionPagerAdapter(private val questions: List<QuizQuestion>, private val onAnswer: (Int, String) -> Unit) : RecyclerView.Adapter<QuestionPagerAdapter.VH>() {
    inner class VH(val b: ItemQuestionPageBinding) : RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(ItemQuestionPageBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun getItemCount() = questions.size

    override fun onBindViewHolder(h: VH, pos: Int) {
        val q = questions[pos]; val b = h.b; val ctx = h.itemView.context
        b.tvQuestionNumber.text = "Q${pos+1}"; b.tvQuestion.text = q.question
        val btns = listOf(b.btnOptionA, b.btnOptionB, b.btnOptionC, b.btnOptionD)
        val labels = listOf("A","B","C","D")
        btns.forEachIndexed { i, btn ->
            if (i < q.options.size) {
                btn.text = "${labels[i]}. ${q.options[i]}"
                val sel = q.userAnswer == q.options[i]
                btn.backgroundTintList = ctx.getColorStateList(if(sel) R.color.color_primary else R.color.color_option_default)
                btn.setTextColor(ctx.getColor(if(sel) android.R.color.white else R.color.color_on_surface))
                btn.setOnClickListener { q.userAnswer=q.options[i]; q.isAnswered=true; notifyItemChanged(pos); onAnswer(pos,q.options[i]) }
            }
        }
    }
    fun getSelectedAnswer(pos: Int) = questions.getOrNull(pos)?.userAnswer
}
