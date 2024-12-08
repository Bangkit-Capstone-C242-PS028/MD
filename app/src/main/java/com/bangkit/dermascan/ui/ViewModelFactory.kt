package com.bangkit.dermascan.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.dermascan.data.repository.ApiRepository
import com.bangkit.dermascan.data.repository.UserRepository
import com.bangkit.dermascan.di.Injection

import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.ui.articleAdd.ArticleAddViewModel
import com.bangkit.dermascan.ui.articleDetail.ArticleDetailViewModel
import com.bangkit.dermascan.ui.forum.ForumViewModel
import com.bangkit.dermascan.ui.forumAdd.ForumAddViewModel
import com.bangkit.dermascan.ui.forumDetail.ForumDetailViewModel

import com.bangkit.dermascan.ui.main.MainViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val apiRepository: ApiRepository,
    private val application: Application
):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, apiRepository) as T
            }
//            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
//                AuthViewModel(repository, apiRepository) as T
//            }
//            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
//                RegisterViewModel(apiRepository) as T
//            }
//            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
//                DetailViewModel(apiRepository) as T
//            }
//            modelClass.isAssignableFrom(SkinLesionViewModel::class.java) -> {
//                SkinLesionViewModel(apiRepository) as T
//            }

            modelClass.isAssignableFrom(ArticleViewModel::class.java) -> {
                ArticleViewModel(apiRepository) as T
            }

            modelClass.isAssignableFrom(ArticleDetailViewModel::class.java) -> {
                ArticleDetailViewModel(apiRepository, application) as T
            }

            modelClass.isAssignableFrom(ArticleAddViewModel::class.java) -> {
                ArticleAddViewModel(apiRepository) as T
            }
            modelClass.isAssignableFrom(ForumViewModel::class.java) -> {
                ForumViewModel(apiRepository) as T
            }

            modelClass.isAssignableFrom(ForumDetailViewModel::class.java) -> {
                ForumDetailViewModel(apiRepository) as T
            }

            modelClass.isAssignableFrom(ForumAddViewModel::class.java) -> {
                ForumAddViewModel(apiRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val application = context.applicationContext as Application
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideApiRepository(context),
                        application
                    )

                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}