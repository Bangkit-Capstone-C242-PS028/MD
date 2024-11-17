package com.bangkit.dermascan.data.model

import com.google.gson.annotations.SerializedName

data class SignUpRequest(

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

	@field:SerializedName("specialization")
	val specialization: String,

	@field:SerializedName("workplace")
	val workplace: String? = null,

	@field:SerializedName("email")
	val email: String
)
