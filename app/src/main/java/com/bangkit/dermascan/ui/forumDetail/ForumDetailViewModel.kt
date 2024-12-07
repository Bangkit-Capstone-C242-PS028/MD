package com.bangkit.dermascan.ui.forumDetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.response.ForumDetail
import com.bangkit.dermascan.data.model.response.ForumReply
import com.bangkit.dermascan.data.repository.ApiRepository
import kotlinx.coroutines.launch

class ForumDetailViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {
    private val _forum = MutableLiveData<ForumDetail>()
    val forum: LiveData<ForumDetail> = _forum

    private val _replies = MutableLiveData<List<ForumReply>>()
    val replies: LiveData<List<ForumReply>> = _replies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    fun showForumDetail(id: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = apiRepository.getForumDetail(id)
                _forum.value = response.data!!
                fetchForumReplies(id)
                Log.d("ForumDetailViewModel", "Forum details loaded successfully!")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("ForumDetailViewModel", "Error loading forum details: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendForumReply(forumId: String, content: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                apiRepository.createForumReply(forumId, content)
                fetchForumReplies(forumId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }


    private suspend fun fetchForumReplies(forumId: String) {
        try {
            val repliesResponse = apiRepository.getForumReplies(forumId)
            _replies.value = repliesResponse.data.data
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }
}