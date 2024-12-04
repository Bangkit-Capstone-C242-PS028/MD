package com.bangkit.dermascan.ui.articleDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.response.Article
import com.bangkit.dermascan.data.repository.ApiRepository

import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article> = _article

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun showArticleDetail(id: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = apiRepository.getArticleDetail(id)
                _article.value = response.article!!
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}