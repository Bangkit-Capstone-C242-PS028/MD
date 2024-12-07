package com.bangkit.dermascan.data.pref

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
    val workplace: String?= null,
    val token: String?= null  ,
    val isLogin: Boolean = false,
)

