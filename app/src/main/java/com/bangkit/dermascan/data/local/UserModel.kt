package com.bangkit.dermascan.data.local

data class UserModel(
    val uid: String?= null,
    val firstName: String?= null,
    val lastName: String?= null,
    val role: String?= null,
    val dob: String?= null,
    val address: String?= null,
    val specialization: String?= null,
    val email: String,
    val workplace: String?= null,
    val token: String?= null  ,
    val isLogin: Boolean = false,
)

