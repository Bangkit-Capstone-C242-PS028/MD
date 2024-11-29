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
