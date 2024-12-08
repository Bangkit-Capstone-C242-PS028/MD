package com.bangkit.dermascan.ui.articleDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.database.FavoriteArticle
import com.bangkit.dermascan.data.model.response.ArticleDetail
import com.bangkit.dermascan.data.repository.ApiRepository
import com.bangkit.dermascan.data.repository.FavoriteArticleRepository
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val apiRepository: ApiRepository,
    application: Application
) : ViewModel() {
    private val mFavoriteArticleRepository: FavoriteArticleRepository =
        FavoriteArticleRepository(application)

    private val _article = MutableLiveData<ArticleDetail>()
    val article: LiveData<ArticleDetail> = _article

    private val _favoriteArticle = MutableLiveData<FavoriteArticle?>()
    val favoriteArticle: LiveData<FavoriteArticle?> = _favoriteArticle

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
                if (response.data != null) {
                    _article.value = response.data!!

                    // Cek favorit artikel
                    mFavoriteArticleRepository.getFavoriteArticleById(id).observeForever { favoriteArticle ->
                        _favoriteArticle.value = favoriteArticle
                    }
                } else {
                    _errorMessage.value = "Article not found"
                }
            } catch (e: Exception) {
                Log.e("ArticleDetailViewModel", "Error loading article: ${e.message}", e)
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertFavoriteArticle(articleDetail: ArticleDetail) {
        val favoriteArticle = FavoriteArticle(
            id = articleDetail.id ?: "",
            title = articleDetail.title,
            content = articleDetail.content,
            imageUrl = articleDetail.imageUrl,
            createdAt = articleDetail.createdAt,
            updatedAt = articleDetail.updatedAt,
            name = articleDetail.name,
            avatar = articleDetail.avatar?.toString()
        )
        mFavoriteArticleRepository.insert(favoriteArticle)
    }

    fun deleteFavoriteArticle(favoriteArticle: FavoriteArticle) {
        mFavoriteArticleRepository.delete(favoriteArticle)
    }
}