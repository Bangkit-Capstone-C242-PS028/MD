//package com.bangkit.dermascan.data.model.response
//
//import com.google.gson.annotations.SerializedName
//
//data class Response(
//
//	@field:SerializedName("data")
//	val data: Data? = null,
//
//	@field:SerializedName("message")
//	val message: String? = null,
//
//	@field:SerializedName("statusCode")
//	val statusCode: Int? = null
//)
//
//data class User(
//
//	@field:SerializedName("uid")
//	val uid: String? = null,
//
//	@field:SerializedName("firstName")
//	val firstName: String? = null,
//
//	@field:SerializedName("lastName")
//	val lastName: String? = null,
//
//	@field:SerializedName("createdAt")
//	val createdAt: String? = null,
//
//	@field:SerializedName("role")
//	val role: String? = null,
//
//	@field:SerializedName("address")
//	val address: String? = null,
//
//	@field:SerializedName("dob")
//	val dob: String? = null,
//
//	@field:SerializedName("email")
//	val email: String? = null,
//
//	@field:SerializedName("updatedAt")
//	val updatedAt: String? = null,
//
//	@field:SerializedName("points")
//	val points: Int? = null
//)
//
//data class Author(
//
//	@field:SerializedName("uid")
//	val uid: String? = null,
//
//	@field:SerializedName("isVerified")
//	val isVerified: Boolean? = null,
//
//	@field:SerializedName("documentUrl")
//	val documentUrl: String? = null,
//
//	@field:SerializedName("specialization")
//	val specialization: String? = null,
//
//	@field:SerializedName("workplace")
//	val workplace: String? = null,
//
//	@field:SerializedName("user")
//	val user: User? = null
//)
//
//data class DataItem(
//
//	@field:SerializedName("updated_at")
//	val updatedAt: String? = null,
//
//	@field:SerializedName("author")
//	val author: Author? = null,
//
//	@field:SerializedName("imageUrl")
//	val imageUrl: String? = null,
//
//	@field:SerializedName("created_at")
//	val createdAt: String? = null,
//
//	@field:SerializedName("id")
//	val id: String? = null,
//
//	@field:SerializedName("title")
//	val title: String? = null,
//
//	@field:SerializedName("content")
//	val content: String? = null
//)
//
////data class Meta(
////
////	@field:SerializedName("total")
////	val total: Int? = null,
////
////	@field:SerializedName("lastPage")
////	val lastPage: Int? = null,
////
////	@field:SerializedName("page")
////	val page: Int? = null
////)
//
//data class Data(
//
//	@field:SerializedName("data")
//	val data: List<DataItem?>? = null,
//
//	@field:SerializedName("meta")
//	val meta: Meta? = null
//)
