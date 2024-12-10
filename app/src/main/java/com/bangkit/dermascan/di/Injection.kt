package com.bangkit.dermascan.di

import android.content.Context
import com.bangkit.dermascan.data.pref.UserPreference
import com.bangkit.dermascan.data.repository.UserRepository
//import com.bangkit.dermascan.dataArticles.local.com.bangkit.dermascan.data.pref.UserPreference
//import com.bangkit.dermascan.dataArticles.local.com.bangkit.dermascan.data.pref.getDataStore
import com.bangkit.dermascan.data.remote.service.ApiConfig
import com.bangkit.dermascan.data.repository.ApiRepository
//import com.bangkit.dermascan.util.com.bangkit.dermascan.data.pref.getDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.bangkit.dermascan.data.pref.dataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Injection {

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    @Provides
    @Singleton
    fun provideApiRepository(@ApplicationContext context: Context): ApiRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val api = ApiConfig(pref,context).apiService
        return ApiRepository(api)
    }

}