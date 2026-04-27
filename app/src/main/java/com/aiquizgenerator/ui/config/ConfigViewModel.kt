package com.aiquizgenerator.ui.config
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiquizgenerator.data.remote.model.QuizConfig
import com.aiquizgenerator.data.remote.model.QuizQuestion
import com.aiquizgenerator.repository.QuizRepository
import com.aiquizgenerator.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(private val repo: QuizRepository) : ViewModel() {
    private val _state = MutableLiveData<Resource<List<QuizQuestion>>>()
    val state: LiveData<Resource<List<QuizQuestion>>> = _state
    fun generate(config: QuizConfig) { _state.value = Resource.Loading; viewModelScope.launch { _state.value = repo.generateQuiz(config) } }
}
