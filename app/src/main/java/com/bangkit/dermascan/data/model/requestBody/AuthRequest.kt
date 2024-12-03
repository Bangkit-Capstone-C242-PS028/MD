package com.bangkit.dermascan.data.model.requestBody

import com.google.gson.annotations.SerializedName

data class AuthRequest(

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("dob")
	val dob: String,

	@field:SerializedName("confirmPassword")
	val confirmPassword: String,

	@field:SerializedName("specialization") // buat dokter only
	val specialization: String? = null,

	@field:SerializedName("workplace") // buat dokter only
	val workplace: String? = null,

	@field:SerializedName("email")
	val email: String
)

data class DoctorSignupRequest(
	val role: String,
	val email: String,
	val password: String,
	val confirmPassword: String,
	val firstName: String,
	val lastName: String,
	val dob: String,
	val address: String,
	val workplace: String,
	val specialization: String,
	val whatsappUrl: String
)

