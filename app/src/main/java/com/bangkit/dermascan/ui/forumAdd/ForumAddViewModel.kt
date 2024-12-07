package com.bangkit.dermascan.ui.forumAdd

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.requestBody.ForumRequest
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

    fun createForum(title: String, content: String) {
        _isLoading.value = true
        _errorMessage.value = null

        // Validasi input
        if (title.trim().isEmpty() || content.trim().isEmpty()) {
            _errorMessage.value = "Title and content cannot be empty"
            _isLoading.value = false
            _uploadResult.value = false
            return
        }

        viewModelScope.launch {
            try {
                val forumRequest = ForumRequest(title.trim(), content.trim())
                apiRepository.createForum(forumRequest)
                _uploadResult.value = true
            } catch (e: Exception) {
                Log.e("ForumAddViewModel", "Forum creation error", e)
                _errorMessage.value = e.localizedMessage ?: "Unknown error occurred"
                _uploadResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}