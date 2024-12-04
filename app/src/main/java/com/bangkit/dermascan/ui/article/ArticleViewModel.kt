package com.bangkit.dermascan.ui.article

import UserPreference
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.response.ListArticleItem
import com.bangkit.dermascan.data.repository.ApiRepository
//import com.bangkit.dermascan.data.repository.ArticleRepository
import com.bangkit.dermascan.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val apiRepository: ApiRepository) : ViewModel() {

    private val _listArticle = MutableLiveData<List<ListArticleItem>>()
    val listArticle: LiveData<List<ListArticleItem>> = _listArticle

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
                _listArticle.value = response.listArticle
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

//    fun addArticle(title: String, content: String, imageFile: File) {
//        _isLoading.value = true
//        _errorMessage.value = null
//        viewModelScope.launch {
//            try {
//                val response = repository.addArticle(title, content, imageFile)
//                _addArticleResponse.value = response
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

//    fun logout() {
//        viewModelScope.launch {
//            userPreference.clearToken()
//        }
//    }
}