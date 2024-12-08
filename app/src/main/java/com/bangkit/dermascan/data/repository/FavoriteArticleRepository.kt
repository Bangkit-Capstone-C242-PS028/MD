package com.bangkit.dermascan.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.bangkit.dermascan.data.database.FavoriteArticle
import com.bangkit.dermascan.data.database.FavoriteArticleDao
import com.bangkit.dermascan.data.database.FavoriteArticleDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteArticleRepository(application: Application) {
    private val mFavoriteArticleDao: FavoriteArticleDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteArticleDatabase.getDatabase(application)
        mFavoriteArticleDao = db.favoriteArticleDao()
    }

    fun getAllFavoriteArticles(): LiveData<List<FavoriteArticle>> =
        mFavoriteArticleDao.getAllFavoriteArticles()

    fun insert(favoriteArticle: FavoriteArticle) {
        executorService.execute { mFavoriteArticleDao.insert(favoriteArticle) }
    }

    fun getFavoriteArticleById(id: String): LiveData<FavoriteArticle> {
        return mFavoriteArticleDao.getFavoriteArticleById(id)
    }

    fun delete(favoriteArticle: FavoriteArticle) {
        executorService.execute { mFavoriteArticleDao.delete(favoriteArticle) }
    }
}