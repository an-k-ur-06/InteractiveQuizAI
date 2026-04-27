package com.aiquizgenerator.ui.config
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aiquizgenerator.R
import com.aiquizgenerator.data.remote.model.Difficulty
import com.aiquizgenerator.data.remote.model.QuizConfig
import com.aiquizgenerator.databinding.FragmentConfigBinding
import com.aiquizgenerator.utils.Resource
import com.aiquizgenerator.utils.gone
import com.aiquizgenerator.utils.toast
import com.aiquizgenerator.utils.visible
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigFragment : Fragment() {
    private var _b: FragmentConfigBinding? = null
    private val b get() = _b!!
    private val vm: ConfigViewModel by viewModels()
    private var diff = Difficulty.MEDIUM; private var numQ = 10; private var timer = 15
    private val extractedText by lazy { arguments?.getString("extractedText") ?: "" }
    private val fileName by lazy { arguments?.getString("fileName") ?: "" }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentConfigBinding.inflate(i,c,false).also{_b=it}.root

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v,s)
        b.tvSourceFile.text = fileName
        b.chipEasy.setOnClickListener { setDiff(Difficulty.EASY) }
        b.chipMedium.setOnClickListener { setDiff(Difficulty.MEDIUM) }
        b.chipHard.setOnClickListener { setDiff(Difficulty.HARD) }
        setDiff(Difficulty.MEDIUM)
        b.seekbarQuestions.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb:SeekBar?,p:Int,u:Boolean){numQ=maxOf(5,p);b.tvQuestionsCount.text="$numQ"}
            override fun onStartTrackingTouch(sb:SeekBar?){}; override fun onStopTrackingTouch(sb:SeekBar?){}
        })
        b.seekbarTimer.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb:SeekBar?,p:Int,u:Boolean){timer=maxOf(5,p);b.tvTimerValue.text="$timer min"}
            override fun onStartTrackingTouch(sb:SeekBar?){}; override fun onStopTrackingTouch(sb:SeekBar?){}
        })
        b.btnGenerateQuiz.setOnClickListener {
            if(extractedText.isBlank()){requireContext().toast("No text found");return@setOnClickListener}
            vm.generate(QuizConfig(diff,numQ,timer,extractedText,fileName))
        }
        vm.state.observe(viewLifecycleOwner){state->
            when(state){
                is Resource.Loading->{b.loadingLayout.visible();b.btnGenerateQuiz.isEnabled=false}
                is Resource.Success->{b.loadingLayout.gone();b.btnGenerateQuiz.isEnabled=true
                    findNavController().navigate(R.id.action_config_to_quiz, bundleOf("questionsJson" to Gson().toJson(state.data),"timerDuration" to timer,"fileName" to fileName,"difficulty" to diff.name))}
                is Resource.Error->{b.loadingLayout.gone();b.btnGenerateQuiz.isEnabled=true;requireContext().toast(state.message)}
            }
        }
    }
    private fun setDiff(d:Difficulty){diff=d;b.chipEasy.isChecked=d==Difficulty.EASY;b.chipMedium.isChecked=d==Difficulty.MEDIUM;b.chipHard.isChecked=d==Difficulty.HARD}
    override fun onDestroyView(){super.onDestroyView();_b=null}
}
