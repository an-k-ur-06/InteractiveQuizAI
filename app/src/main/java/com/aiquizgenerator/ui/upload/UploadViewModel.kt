package com.aiquizgenerator.ui.upload
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiquizgenerator.utils.FileExtractor
import com.aiquizgenerator.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableLiveData<Resource<Pair<String,String>>>()
    val state: LiveData<Resource<Pair<String,String>>> = _state
    var extractedText = ""; var fileName = ""

    fun processFile(ctx: Context, uri: Uri, mime: String?) {
        _state.value = Resource.Loading
        viewModelScope.launch {
            val r = withContext(Dispatchers.IO) { FileExtractor.extractText(ctx, uri, mime) }
            r.fold(
                onSuccess = { text -> extractedText = text; fileName = FileExtractor.getFileName(ctx, uri); _state.value = Resource.Success(fileName to text) },
                onFailure = { _state.value = Resource.Error(it.localizedMessage ?: "Failed") }
            )
        }
    }
}
