package com.bangkit.dermascan.ui.forumAdd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.repository.ApiRepository
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class ForumAddViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> = _uploadResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    suspend fun createForum(
        title: RequestBody,
        content: RequestBody
    ) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                apiRepository.createForum(title, content)
                _uploadResult.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _uploadResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}