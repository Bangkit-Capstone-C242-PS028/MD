//package com.bangkit.dermascan.data.repository
//
//import UserPreference
//
//import android.util.Log
//import com.bangkit.dermascan.data.model.response.ArticleResponse
//import com.bangkit.dermascan.data.model.response.ErrorResponse
//import com.bangkit.dermascan.data.remote.service.ApiService
//import com.google.gson.Gson
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import retrofit2.HttpException
//import java.io.File
//
//
//class ArticleRepository(
//    private val apiService: ApiService,
//    private val userPreference: UserPreference
//) {
//    suspend fun getArticles(): ArticleResponse {
//        return try {
//            val response = apiService.getArticles()
//            Log.d("ArticleRepository", "Success: $response")
//            response
//        } catch (e: HttpException) {
//            val jsonInString = e.response()?.errorBody()?.string()
//            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
//            Log.e("ArticleRepository", "Error: ${e.message}")
//            throw Exception(errorBody.message)
//        }
//    }
//
////    suspend fun addArticle(title: String, content: String, imageFile: File): ArticleResponse {
////        return try {
////            val titlePart = title.toRequestBody("text/plain".toMediaType())
////            val contentPart = content.toRequestBody("text/plain".toMediaType())
////            val imagePart = MultipartBody.Part.createFormData(
////                "image",
////                imageFile.name,
////                imageFile.asRequestBody("image/*".toMediaType())
////            )
////
////            val response = apiService.createArticle(titlePart, contentPart, imagePart)
////            Log.d("ArticleRepository", "Success: $response")
////            response
////        } catch (e: HttpException) {
////            val jsonInString = e.response()?.errorBody()?.string()
////            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
////            Log.e("ArticleRepository", "Error: ${e.message}")
////            throw Exception(errorBody.message)
////        }
////    }
//
//    companion object {
//        @Volatile
//        private var instance: ArticleRepository? = null
//
//        fun getInstance(apiService: ApiService, userPreference: UserPreference): ArticleRepository {
//            return instance ?: synchronized(this) {
//                instance ?: ArticleRepository(apiService, userPreference).also { instance = it }
//            }
//        }
//    }
//}