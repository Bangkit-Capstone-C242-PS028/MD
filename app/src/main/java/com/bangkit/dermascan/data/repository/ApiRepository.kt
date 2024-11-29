package com.bangkit.dermascan.data.repository

import android.util.Log
import coil3.Uri
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.*
//import com.bangkit.dermascan.dataArticles.local.UserData
import com.bangkit.dermascan.data.model.requestBody.AuthRequest
import com.bangkit.dermascan.data.model.response.BaseResponse
import com.bangkit.dermascan.data.model.response.SkinLesionItem
//import com.bangkit.dermascan.data.model.response.SkinLesion
import com.bangkit.dermascan.data.model.response.SkinLesionsData
import com.bangkit.dermascan.data.model.response.SkinLesionsResponse
import com.bangkit.dermascan.data.remote.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.IOException

class ApiRepository(private val apiService: ApiService) {
    suspend fun signup(signupRequest: AuthRequest): Result<String> {
        // Menandakan loading
        Result.Loading
        return try {
            val response = apiService.signup(signupRequest)
            if (response.isSuccessful) {
                val signupResponse = response.body()
                if (signupResponse != null) {
                    Result.Success(signupResponse.message)
//                    signupResponse.success?.let {
//                        // Menggunakan success jika ada
//                        Result.Success(it.message)
//                    } ?: signupResponse.error?.let {
//                        // Menggunakan error jika success null
//                        Result.Error(it.message)
//                    } ?: Result.Error("Unknown response format")
                } else {
                    Result.Error("No response body")
                }
            } else {
                // Jika server mengembalikan error, ambil detail error dari body respons
                val errorResponse = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorResponse)
                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun uploadSkinImage(image: File): Result<String> {
        // Membuat RequestBody untuk gambar
//        val requestImageFile = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = prepareFilePart(image)

        return try {
            // Mengirimkan request ke API
            val response = apiService.uploadSkinImage(multipartBody)

            // Memeriksa apakah response sukses
            if (response.isSuccessful) {
                Result.Success("Image uploaded successfully") // Response sukses
            } else {
                // Jika response gagal, kembalikan error message dari response
                val errorMessage = response.errorBody()?.string()
                Result.Error(parseErrorMessage(errorMessage) )
            }
        } catch (e: Exception) {
            // Menangani exception dan mengembalikan error message
            Result.Error("Exception: ${e.message}")
        }
    }



    suspend fun getSkinLesions(
        page: Int = 1,
        limit: Int = 10
    ): Flow<Result<List<SkinLesionItem>>?> = flow {
        try {
            val response = apiService.getSkinLesions(page = page, limit = limit)

            // Check HTTP response code or specific error conditions
            if (response.isSuccessful) {
                Log.d("API Response", "Lesions: ${response.body()?.data?.lesions}")
                emit(response.body()?.data?.let { Result.Success(it.lesions) })
            } else {
                // Parse error body for more detailed error message
                val errorBody = response.errorBody()?.string()
                val errorMessage = when (response.code()) {
                    401 -> "Authentication failed. Please log in again." // Token expired
                    403 -> "Forbidden. You don't have permission."
                    404 -> "Resource not found."
                    500 -> "Server error. Please try again later."
                    else -> errorBody ?: "Unknown error occurred"
                }

                // Log the detailed error
                Log.e("API Error", "Code: ${response.code()}, Message: $errorMessage")

                emit(Result.Error(errorMessage))
            }
        } catch (e: Exception) {
            // Handle network or other exceptions
            val errorMessage = when (e) {
                is IOException -> "Network error. Please check your connection."
                is retrofit2.HttpException -> {
                    // For HTTP exceptions not caught in the response check above
                    val errorBody = e.response()?.errorBody()?.string()
                    errorBody ?: e.message ?: "HTTP error occurred"
                }
                else -> e.message ?: "An unexpected error occurred"
            }

            Log.e("API Exception", "Error: $errorMessage", e)
            emit(Result.Error(parseErrorMessage(errorMessage)))
        }
    }.flowOn(Dispatchers.IO)









}