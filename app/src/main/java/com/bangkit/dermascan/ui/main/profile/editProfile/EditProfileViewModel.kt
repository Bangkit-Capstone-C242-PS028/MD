package com.bangkit.dermascan.ui.main.profile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dermascan.data.model.requestBody.UserRequest
import com.bangkit.dermascan.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(val api: ApiRepository) : ViewModel() {
//    fun updateProfile(userRequest: UserRequest) {
//        viewModelScope.launch {
//            api.updateUser(userRequest)
//
//        }
//    }
    private val _updateUserResult = MutableLiveData<Result<UserResponse>>(Result.Idle)
    val updateUserResult: LiveData<Result<UserResponse>> get() = _updateUserResult

    fun updateUser(firstname: String?, lastname: String?, address: String?, imgFile: File?) {
        viewModelScope.launch {
            if (imgFile != null) {
                // Proses dengan gambar
                // Emit loading state
                _updateUserResult.value = Result.Loading

                // Call repository function
                val result = api.updateUser(firstname, lastname, address, imgFile)

                // Emit result
                _updateUserResult.value = result
            } else {
                // Proses tanpa gambar
                // Emit loading state
                _updateUserResult.value = Result.Loading

                // Call repository function
                val result = api.updateUser(firstname = firstname, lastname = lastname, address = address)

                // Emit result
                _updateUserResult.value = result
            }

        }
    }
}