package com.bangkit.dermascan.ui.main.profile.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.bangkit.dermascan.util.Result

@HiltViewModel
class ConsulViewModel @Inject constructor(private val userRepository: ApiRepository) : ViewModel()  {

    private val _doctors = MutableLiveData<Result<List<UserData>>>()
    val doctors: LiveData<Result<List<UserData>>> get() = _doctors

    // Fungsi untuk mengambil daftar dokter
    fun getDoctors(page: Int = 1, size: Int = 10) {
        _doctors.value = Result.Loading

        viewModelScope.launch {
            try {
                val response = userRepository.getDoctors(page, size)
                if (response.isSuccessful && response.body() != null) {
                    _doctors.value = Result.Success(response.body()!!.data?.users ?: emptyList())
                } else {
                    _doctors.value = Result.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _doctors.value = Result.Error("Error: ${e.message}")
            }
        }
    }
}