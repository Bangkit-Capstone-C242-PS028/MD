package com.bangkit.dermascan.ui.upload

import android.net.Uri
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : ViewModel() {
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri = _imageUri.asStateFlow()

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }
}