package com.bangkit.dermascan.ui.article

import UserPreference
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.response.ArticleItem
import com.bangkit.dermascan.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _listArticle = MutableLiveData<List<ArticleItem>>()
    val listArticle: LiveData<List<ArticleItem>> = _listArticle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
//
//    private val _addArticleResponse = MutableLiveData<ArticleResponse>()
//    val addArticleResponse: LiveData<ArticleResponse> = _addArticleResponse

    fun showArticles() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = apiRepository.getArticles()
                _listArticle.value = response.data?.articles ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
