package com.bangkit.dermascan.ui

import UserPreference
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.dermascan.data.repository.ApiRepository
import com.bangkit.dermascan.data.repository.UserRepository
import com.bangkit.dermascan.di.Injection
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.ui.articleAdd.ArticleAddViewModel
import com.bangkit.dermascan.ui.articleDetail.ArticleDetailViewModel

class ViewModelFactory(

    private val repository: UserRepository,
    private val apiRepository: ApiRepository,
//    private val articleRepository: ArticleRepository? = null,
//    private val articleDetailRepository: ArticleDetailRepository? = null,
//    private val articleAddRepository: ArticleAddRepository? = null,
//    private val userPreference: UserPreference? = null
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
                ArticleDetailViewModel(apiRepository) as T
            }

            modelClass.isAssignableFrom(ArticleAddViewModel::class.java) -> {
                ArticleAddViewModel(apiRepository) as T
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
                    INSTANCE = ViewModelFactory(Injection.provideUserRepository(context),Injection.provideApiRepository(context))

                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}