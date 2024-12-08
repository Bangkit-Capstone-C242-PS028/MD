package com.bangkit.dermascan.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserData ?= null,
)

data class GetDoctorResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserDataList ?= null,
)

data class UserDataList(
    @SerializedName("data") val users: List<UserData>,
    @SerializedName("meta") val meta: Meta
)

data class UserData(
    @SerializedName("uid") val uid: String,
    @SerializedName("role") val role: String,
    @SerializedName("email") val email: String,
    @SerializedName("firstName") val firstName: String?=null,
    @SerializedName("lastName") val lastName: String?=null,
    @SerializedName("dob") val dob: String,
    @SerializedName("address") val address: String?=null,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("photoUrl") val photoUrl: String?=null,
    @SerializedName("points") val points: Int ?= 0,
    @SerializedName("doctor") val doctor: DoctorData?=null,   // Bisa null jika role adalah PATIENT
    @SerializedName("patient") val patient: PatientData?=null,  // Bisa null jika role adalah DOCTOR
)

data class DoctorData(
    @SerializedName("uid") val uid: String,
    @SerializedName("specialization") val specialization: String, // Tidak ada dalam respons ini
    @SerializedName("workplace") val workplace: String,
    @SerializedName("documentUrl") val documentUrl: String,
    @SerializedName("isVerified") val isVerified: Boolean,
    @SerializedName("whatsappUrl") val whatsappUrl: String,
)

data class PatientData(
    @SerializedName("uid") val uid: String
)

data class LoginResponse(
    val statusCode: Int,
    val message: String,
    val data: TokenData
)

data class TokenData(
    val token: String
)

data class LoginRequest(
    val email: String,
    val password: String
)





