package com.aiquizgenerator.ui.quiz
import android.app.AlertDialog; import android.os.Bundle
import android.view.LayoutInflater; import android.view.View; import android.view.ViewGroup
import androidx.core.os.bundleOf; import androidx.fragment.app.Fragment; import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController; import androidx.recyclerview.widget.GridLayoutManager
import com.aiquizgenerator.R; import com.aiquizgenerator.data.remote.model.QuizQuestion
import com.aiquizgenerator.databinding.FragmentQuizBinding; import com.aiquizgenerator.utils.formatTimer
import com.google.gson.Gson; import com.google.gson.reflect.TypeToken; import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment() {
    private var _b: FragmentQuizBinding? = null; private val b get() = _b!!
    private val vm: QuizViewModel by viewModels()
    private lateinit var pagerAdapter: QuestionPagerAdapter; private lateinit var navAdapter: QuestionNavGridAdapter
    private val questionsJson by lazy { arguments?.getString("questionsJson") ?: "[]" }
    private val timerDuration by lazy { arguments?.getInt("timerDuration") ?: 15 }
    private val fileName by lazy { arguments?.getString("fileName") ?: "" }
    private val difficulty by lazy { arguments?.getString("difficulty") ?: "MEDIUM" }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentQuizBinding.inflate(i,c,false).also{_b=it}.root

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)
        val questions: List<QuizQuestion> = Gson().fromJson(questionsJson, object : TypeToken<List<QuizQuestion>>(){}.type)
        vm.initialize(questions, timerDuration, fileName, difficulty)
        pagerAdapter = QuestionPagerAdapter(questions){idx,ans->vm.selectAnswer(idx,ans);navAdapter.notifyDataSetChanged()}
        b.viewPagerQuiz.adapter=pagerAdapter; b.viewPagerQuiz.isUserInputEnabled=false
        navAdapter=QuestionNavGridAdapter(questions){idx->vm.goToQuestion(idx);b.drawerLayout.closeDrawers()}
        b.rvQuestionNav.layoutManager=GridLayoutManager(requireContext(),5); b.rvQuestionNav.adapter=navAdapter
        b.btnPrevious.setOnClickListener{vm.goPrevious()}
        b.btnNext.setOnClickListener{vm.goNext()}
        b.btnSaveNext.setOnClickListener{val i=vm.currentIndex.value?:0;vm.saveAndNext(i,pagerAdapter.getSelectedAnswer(i));navAdapter.notifyDataSetChanged()}
        b.btnMarkReview.setOnClickListener{val i=vm.currentIndex.value?:0;vm.toggleMarkForReview(i);navAdapter.notifyDataSetChanged();updateMarkBtn(i)}
        b.btnSubmit.setOnClickListener{AlertDialog.Builder(requireContext()).setTitle("Submit Quiz?").setMessage("Answered: ${vm.getAnsweredCount()}/${vm.questions.size}\nUnanswered: ${vm.getUnansweredCount()}\n\nSure?").setPositiveButton("Submit"){_,_->vm.submitQuiz()}.setNegativeButton("Cancel",null).show()}
        b.btnOpenNav.setOnClickListener{b.drawerLayout.openDrawer(b.navPanel)}
        vm.currentIndex.observe(viewLifecycleOwner){i->b.viewPagerQuiz.currentItem=i;updateProgress(i);updateMarkBtn(i);b.btnPrevious.isEnabled=i>0;b.btnNext.isEnabled=i<vm.questions.size-1}
        vm.timerSeconds.observe(viewLifecycleOwner){t->b.tvTimer.text=t.formatTimer();b.tvTimer.setTextColor(requireContext().getColor(if(t<60) R.color.color_error else R.color.color_primary))}
        vm.quizSubmitted.observe(viewLifecycleOwner){if(it)vm.quizResult.value?.let{r->findNavController().navigate(R.id.action_quiz_to_results,bundleOf("resultJson" to Gson().toJson(r)))}}
    }

    private fun updateProgress(i: Int){b.tvQuestionProgress.text="Question ${i+1} of ${vm.questions.size}";b.progressBar.max=vm.questions.size;b.progressBar.progress=i+1;b.tvAnswered.text="Answered: ${vm.getAnsweredCount()}";b.tvMarked.text="Marked: ${vm.getMarkedCount()}";b.tvSkipped.text="Skipped: ${vm.getUnansweredCount()}"}
    private fun updateMarkBtn(i: Int) {
        val m = vm.questions.getOrNull(i)?.isMarkedForReview ?: false

        b.btnMarkReview.text =
            if (m) "Unmark Review"
            else "Mark for Review"

        b.btnMarkReview.backgroundTintList =
            requireContext().getColorStateList(
                if (m) R.color.color_marked
                else R.color.color_surface_variant
            )
    }
}
