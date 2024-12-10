package com.bangkit.dermascan.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

//val Context.com.bangkit.dermascan.data.pref.getDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//val Context.dataStoree by preferencesDataStore(name = "settings")
//val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("settings")
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

//    private val Context.dataStore by preferencesDataStore(name = "settings")


    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[DARK_THEME_KEY] ?: false }

    suspend fun toggleTheme() {
        context.dataStore.edit { preferences ->
            val currentTheme = preferences[DARK_THEME_KEY] ?: false
            preferences[DARK_THEME_KEY] = !currentTheme
        }
    }

    companion object {
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

}
