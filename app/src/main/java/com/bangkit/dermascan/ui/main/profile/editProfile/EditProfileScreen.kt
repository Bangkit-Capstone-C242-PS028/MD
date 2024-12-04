package com.bangkit.dermascan.ui.main.profile.editProfile

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.bangkit.dermascan.ui.authentication.AuthViewModel

@Composable
fun EditProfileScreen(){
    val authViewModel: AuthViewModel = hiltViewModel()
    val editProfileViewModel : EditProfileViewModel = hiltViewModel()


//    val user by authViewModel.user.observeAsState()
}