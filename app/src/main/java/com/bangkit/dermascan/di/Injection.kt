package com.bangkit.dermascan.di

import android.content.Context
import com.bangkit.dermascan.data.repository.UserRepository
//import com.bangkit.dermascan.dataArticles.local.UserPreference
//import com.bangkit.dermascan.dataArticles.local.dataStore
import com.bangkit.dermascan.data.remote.service.ApiConfig
import com.bangkit.dermascan.data.repository.ApiRepository
//import com.bangkit.dermascan.util.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dataStore
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