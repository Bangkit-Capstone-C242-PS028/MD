package com.bangkit.dermascan.ui.articleDetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.database.FavoriteArticle
import com.bangkit.dermascan.data.model.response.ArticleDetail
import com.bangkit.dermascan.data.repository.ApiRepository
import com.bangkit.dermascan.data.repository.FavoriteArticleRepository
import com.bangkit.dermascan.util.Result
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val apiRepository: ApiRepository,
    application: Application
) : ViewModel() {
    private val mFavoriteArticleRepository: FavoriteArticleRepository =
        FavoriteArticleRepository(application)

    private val _article = MutableLiveData<ArticleDetail?>()
    val article: MutableLiveData<ArticleDetail?> = _article

    private val _favoriteArticle = MutableLiveData<FavoriteArticle?>()
    val favoriteArticle: LiveData<FavoriteArticle?> = _favoriteArticle

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _favoriteStatus = MutableLiveData<Result<String>>()
    val favoriteStatus: LiveData<Result<String>> = _favoriteStatus

    fun getFavoriteArticleById(id: String): LiveData<FavoriteArticle> {
        return mFavoriteArticleRepository.getFavoriteArticleById(id)
    }
    fun showArticleDetail(id: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = apiRepository.getArticleDetail(id)
                if (response.data != null) {
                    _article.value = response.data

                    val observer = Observer<FavoriteArticle?> { favoriteArticle ->
                        _favoriteArticle.value = favoriteArticle
                    }
                    mFavoriteArticleRepository.getFavoriteArticleById(id).observeForever(observer)
// Remove observer after setting value
                    mFavoriteArticleRepository.getFavoriteArticleById(id).removeObserver(observer)
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
        viewModelScope.launch {
            try {
                // First, add to local database
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

                // Then, add to remote server
                val apiResult = apiRepository.addToFavorites(articleDetail.id ?: "")
                _favoriteStatus.value = Result.Success(apiResult.message)
            } catch (e: Exception) {
                Log.e("ArticleDetailViewModel", "Error adding to favorites: ${e.message}", e)
                _favoriteStatus.value = Result.Error(e.message ?: "Failed to add to favorites")
            }
        }
    }

    fun deleteFavoriteArticle(favoriteArticle: FavoriteArticle) {
        viewModelScope.launch {
            try {
                // First, remove from local database
                mFavoriteArticleRepository.delete(favoriteArticle)

                // Then, remove from remote server
                val apiResult = apiRepository.removeFromFavorites(favoriteArticle.id)
                _favoriteStatus.value = Result.Success(apiResult.message)
            } catch (e: Exception) {
                Log.e("ArticleDetailViewModel", "Error removing from favorites: ${e.message}", e)
                _favoriteStatus.value = Result.Error(e.message ?: "Failed to remove from favorites")
            }
        }
    }
}