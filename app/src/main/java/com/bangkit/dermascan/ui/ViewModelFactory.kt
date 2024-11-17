package com.bangkit.dermascan.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.dermascan.data.repository.UserRepository
import com.bangkit.dermascan.di.Injection

class ViewModelFactory(private val repository: UserRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
//            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
//                LoginViewModel(repository, apiRepository) as T
//            }
//            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
//                RegisterViewModel(apiRepository) as T
//            }
//            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
//                DetailViewModel(apiRepository) as T
//            }
//            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
//                UploadViewModel(apiRepository) as T
//            }
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
                    INSTANCE = ViewModelFactory(Injection.provideUserRepository(context))
//                    ,Injection.provideApiRepository(context)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}