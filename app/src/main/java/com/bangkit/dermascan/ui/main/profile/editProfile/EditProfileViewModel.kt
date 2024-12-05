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
}