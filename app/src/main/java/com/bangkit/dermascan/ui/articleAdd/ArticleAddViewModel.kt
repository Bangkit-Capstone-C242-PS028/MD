package com.bangkit.dermascan.ui.articleAdd

import UserPreference
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.repository.ApiRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ArticleAddViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> = _uploadResult

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    suspend fun createArticle(
        title: RequestBody,
        content: RequestBody,
        image: MultipartBody.Part
    ) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                apiRepository.createArticle(title, content, image)
                _uploadResult.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _uploadResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }
}