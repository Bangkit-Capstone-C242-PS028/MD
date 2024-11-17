package com.bangkit.dermascan.di

import android.content.Context
import com.bangkit.dermascan.data.repository.UserRepository
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Injection {

//    @Provides
//    fun provideUserPreference(userPreference: UserPreference): Us {
//        return userPreference
//    }

    @Provides
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

}