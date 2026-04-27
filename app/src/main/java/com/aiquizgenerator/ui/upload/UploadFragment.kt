package com.aiquizgenerator.ui.upload
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aiquizgenerator.R
import com.aiquizgenerator.databinding.FragmentUploadBinding
import com.aiquizgenerator.utils.Resource
import com.aiquizgenerator.utils.gone
import com.aiquizgenerator.utils.toast
import com.aiquizgenerator.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFragment : Fragment() {
    private var _b: FragmentUploadBinding? = null
    private val b get() = _b!!
    private val vm: UploadViewModel by viewModels()

    private val picker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { r ->
        if (r.resultCode == Activity.RESULT_OK) r.data?.data?.let { uri ->
            vm.processFile(requireContext(), uri, requireContext().contentResolver.getType(uri))
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?) = FragmentUploadBinding.inflate(i,c,false).also { _b=it }.root

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v,s)
        b.btnPickFile.setOnClickListener { openPicker() }
        b.cardDropZone.setOnClickListener { openPicker() }
        b.btnContinue.setOnClickListener {
            if (vm.extractedText.isNotEmpty())
                findNavController().navigate(R.id.action_upload_to_config, bundleOf("extractedText" to vm.extractedText, "fileName" to vm.fileName))
            else requireContext().toast("Please upload a file first")
        }
        vm.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is Resource.Loading -> { b.progressBar.visible(); b.btnContinue.isEnabled=false; b.tvStatus.text="Processing file..." }
                is Resource.Success -> {
                    b.progressBar.gone(); b.btnContinue.isEnabled=true
                    b.tvFileName.text=state.data.first
                    b.tvWordCount.text="${state.data.second.split("\\s+".toRegex()).size} words extracted"
                    b.tvStatus.text="✓ File processed!"
                    b.cardFileInfo.visibility=View.VISIBLE
                }
                is Resource.Error -> { b.progressBar.gone(); b.tvStatus.text="Error: ${state.message}"; requireContext().toast(state.message) }
            }
        }
    }

    private fun openPicker() {
        picker.launch(Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).apply {
            type="*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf","application/vnd.openxmlformats-officedocument.wordprocessingml.document","text/plain"))
            addCategory(Intent.CATEGORY_OPENABLE)
        }, "Select Study Material"))
    }

    override fun onDestroyView() { super.onDestroyView(); _b=null }
}
