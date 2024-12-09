package com.bangkit.dermascan.ui.articleFavorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.dermascan.data.database.FavoriteArticle
import com.bangkit.dermascan.data.repository.FavoriteArticleRepository

class FavoriteArticleViewModel(application: Application) : ViewModel() {
    private val mFavoriteArticleRepository: FavoriteArticleRepository =
        FavoriteArticleRepository(application)

    fun getFavoriteArticles(): LiveData<List<FavoriteArticle>> {
        return mFavoriteArticleRepository.getAllFavoriteArticles()
    }
}