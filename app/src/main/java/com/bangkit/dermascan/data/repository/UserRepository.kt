package com.bangkit.dermascan.data.repository

import UserPreference
import com.bangkit.dermascan.data.pref.UserModel
//import com.bangkit.dermascan.dataArticles.local.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun saverUpdate(user: UserModel){
        userPreference.saverUpdate(user)
    }
    suspend fun saveUserData(userData: UserModel) {
        return userPreference.saveUserData(userData)
    }

    suspend fun updateToken(newToken: String) {
        userPreference.updateToken(newToken)
    }

    fun getToken(): Flow<String> {
        return userPreference.getToken()
    }

    fun getRoles(): Flow<String> {
        return userPreference.getRoles()
    }

    fun getPoints(): Flow<Int> {
        return userPreference.getPoints()
    }
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

//    suspend fun fetchUserDetail() {
//        userPreference.saveUserData(result.data)
//
//    }
// Fungsi untuk mengambil detail user dari API dan menyimpan di DataStore


    // Fungsi untuk mendapatkan UserData dari DataStore
//    fun getUserData(): Flow<UserData> {
//        return userPreference.getUserData()
//    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}