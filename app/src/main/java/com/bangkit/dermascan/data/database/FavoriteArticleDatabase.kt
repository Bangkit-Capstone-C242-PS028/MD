package com.bangkit.dermascan.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteArticle::class], version = 1)
abstract class FavoriteArticleDatabase : RoomDatabase() {
    abstract fun favoriteArticleDao(): FavoriteArticleDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteArticleDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteArticleDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteArticleDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteArticleDatabase::class.java,
                        "favorite_article"
                    ).build()
                }
            }
            return INSTANCE as FavoriteArticleDatabase
        }
    }
}