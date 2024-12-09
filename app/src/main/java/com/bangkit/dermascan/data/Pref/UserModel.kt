package com.bangkit.dermascan.data.pref

import com.bangkit.dermascan.data.model.response.DoctorData
//import com.bangkit.dermascan.ui.main.profile.result.Doctor
import com.google.gson.annotations.SerializedName

data class UserModel(
    val uid: String?= null,
    val firstName: String?= null,
    val lastName: String?= null,
    val role: String?= null,
    val dob: String?= null,
    val address: String?= null,
    val specialization: String?= null,
    val point: Int ?= null,
    val email: String?=null,
    val profileImageUrl: String?= null,
    val token: String?= null,
    val workplace: String?= null,
    val isLogin: Boolean = false,
    val documentUrl: String?= null,
    val isVerified: Boolean?= null,
    val phoneNumber: String?= null,
    )

