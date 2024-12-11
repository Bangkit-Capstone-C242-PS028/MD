package com.bangkit.dermascan.ui.forum

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.dermascan.data.model.response.ForumItem
import com.bangkit.dermascan.data.repository.ApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

//    val forumsPager : Flow<PagingData<ForumItem>> =
//        apiRepository.getForumPager()
//        .flow
//        .cachedIn(viewModelScope)

    // ViewModel
    private val _forumPager: MutableStateFlow<Flow<PagingData<ForumItem>>> = MutableStateFlow(getForumsPager())
    val forumsPager: StateFlow<Flow<PagingData<ForumItem>>> get() = _forumPager.asStateFlow()

    // ViewModel
    fun getForumsPager(): Flow<PagingData<ForumItem>> {
        return apiRepository.getForumPager()
            .flow
            .cachedIn(viewModelScope)
    }


//    @SuppressLint("CheckResult")
//    fun refreshForums() {
//        // You can trigger a refresh of the pager by invalidating its data
//        forumsPager.cachedIn(viewModelScope)
//    }
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