package com.bangkit.dermascan.ui.upload

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import com.bangkit.dermascan.util.Result
import javax.inject.Inject
import androidx.compose.runtime.State
import com.bangkit.dermascan.data.model.response.SkinLesionItem
//import com.bangkit.dermascan.data.model.response.SkinLesion
import com.bangkit.dermascan.data.model.response.SkinLesionsData
import com.bangkit.dermascan.data.model.response.SkinLesionsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

//import com.bangkit.dermascan.util.Result
@HiltViewModel
class SkinLesionViewModel @Inject constructor(val repository: ApiRepository): ViewModel() {
    private val _uploadResult = mutableStateOf<Result<String>>(Result.Idle) // Inisialisasi dengan Loading
    val uploadResult: State<Result<String>> = _uploadResult
    fun uploadImage(image: File){
        viewModelScope.launch {
            _uploadResult.value = Result.Loading
            val result = repository.uploadSkinImage(image)
            _uploadResult.value = result // Menyimpan hasil upload ke LiveData
        }

    }

    private val _skinLesions = MutableStateFlow<Result<List<SkinLesionItem>>>(Result.Loading)
    val skinLesions: StateFlow<Result<List<SkinLesionItem>>> get() = _skinLesions.asStateFlow()

    fun fetchSkinLesions(page: Int = 1, limit: Int = 10) {
        viewModelScope.launch {
            repository.getSkinLesions(page, limit)
                .onStart { _skinLesions.value = Result.Loading }
                .catch { e -> _skinLesions.value = Result.Error(e.message ?: "Error") }
                .collect { result ->
                    if (result != null) {
                        _skinLesions.value = result
                    }
                }
        }
    }
}