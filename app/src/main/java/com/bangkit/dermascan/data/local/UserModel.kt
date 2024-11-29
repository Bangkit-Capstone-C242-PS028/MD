package com.bangkit.dermascan.data.local

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
)

