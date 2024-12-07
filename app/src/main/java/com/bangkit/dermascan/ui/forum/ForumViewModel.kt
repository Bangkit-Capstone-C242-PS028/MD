package com.bangkit.dermascan.ui.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.response.ForumItem
import com.bangkit.dermascan.data.repository.ApiRepository
import kotlinx.coroutines.launch

class ForumViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _listForum = MutableLiveData<List<ForumItem>>()
    val listForum: LiveData<List<ForumItem>> = _listForum

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun showForums() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = apiRepository.getForums()
                _listForum.value = response.data?.forums ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}