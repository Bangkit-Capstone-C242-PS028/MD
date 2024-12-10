package com.bangkit.dermascan.ui.main.profile.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.pref.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager // Gunakan DataStore untuk menyimpan status tema
) : ViewModel() {

    val isDarkTheme = dataStoreManager.isDarkTheme // Flow<Boolean> untuk memantau tema

    fun toggleTheme() {
        viewModelScope.launch {
            dataStoreManager.toggleTheme() // Mengubah status tema
        }
    }
}