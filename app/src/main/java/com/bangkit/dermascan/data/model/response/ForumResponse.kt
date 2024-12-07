package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName

data class ForumResponse(
    @field:SerializedName("statusCode")
    val statusCode: Int? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("data")
    val data: ForumData? = null
)

data class ForumData(
    @field:SerializedName("data")
    val forums: List<ForumItem>? = null,
    @field:SerializedName("meta")
    val meta: Meta? = null
)

data class ForumItem(
    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("title")
    val title: String? = null,
    @field:SerializedName("content")
    val content: String? = null,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("created_at")
    val createdAt: String? = null,
    @field:SerializedName("updated_at")
    val updatedAt: String? = null,
    @field:SerializedName("patient")
    val patient: PatientInfo? = null,
//    @field:SerializedName("doctor")
//    val doctor: DoctorInfo? = null
)

data class PatientInfo(
    @field:SerializedName("uid")
    val uid: String? = null,
    @field:SerializedName("user")
    val user: UserInfo? = null
)

//data class DoctorInfo(
//    @field:SerializedName("uid")
//    val uid: String? = null,
//    @field:SerializedName("user")
//    val user: UserInfo? = null
//)

data class UserInfo(
    @field:SerializedName("uid")
    val uid: String? = null,
    @field:SerializedName("role")
    val role: String? = null,
    @field:SerializedName("email")
    val email: String? = null,
    @field:SerializedName("firstName")
    val firstName: String? = null,
    @field:SerializedName("lastName")
    val lastName: String? = null
)