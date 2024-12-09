package com.bangkit.dermascan.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteArticle: FavoriteArticle)

    @Query("SELECT * FROM favorite_article WHERE id = :id")
    fun getFavoriteArticleById(id: String): LiveData<FavoriteArticle>

    @Delete
    fun delete(favoriteArticle: FavoriteArticle)

    @Query("SELECT * FROM favorite_article")
    fun getAllFavoriteArticles(): LiveData<List<FavoriteArticle>>
}