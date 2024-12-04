package com.bangkit.dermascan.data.repository

import android.util.Log
import coil3.Uri
import com.bangkit.dermascan.data.model.response.message.SuccessMessage
import com.bangkit.dermascan.data.model.response.message.chat.ChatData
import com.bangkit.dermascan.data.model.response.message.chat.ChatRequest
import com.bangkit.dermascan.data.model.response.message.chat.ChatResponse
import com.bangkit.dermascan.util.Result
import com.bangkit.dermascan.util.*
//import com.bangkit.dermascan.dataArticles.local.UserData
import com.bangkit.dermascan.data.model.requestBody.AuthRequest
import com.bangkit.dermascan.data.model.requestBody.DoctorSignupRequest
import com.bangkit.dermascan.data.model.requestBody.UserRequest

import com.bangkit.dermascan.data.model.response.ArticleDetailResponse
import com.bangkit.dermascan.data.model.response.ArticleResponse
import com.bangkit.dermascan.data.model.response.BaseResponse
import com.bangkit.dermascan.data.model.response.ErrorResponse
import com.bangkit.dermascan.data.model.response.FileUploadResponse
import com.bangkit.dermascan.data.model.response.LoginRequest
import com.bangkit.dermascan.data.model.response.LoginResponse
import com.bangkit.dermascan.data.model.response.SkinLesionItem
//import com.bangkit.dermascan.data.model.response.SkinLesion
import com.bangkit.dermascan.data.model.response.SkinLesionsData
import com.bangkit.dermascan.data.model.response.SkinLesionsResponse
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.data.remote.service.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
//import kotlinx.coroutines.flow.internal.NopCollector.emit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback

class ApiRepository(private val apiService: ApiService) {
    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.login(LoginRequest(email, password))
    }

    // Fungsi untuk mendapatkan token saja dari response
    fun getTokenFromResponse(response: LoginResponse?): String? {
        return response?.data?.token
    }

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

    suspend fun doctorSignup(request: DoctorSignupRequest, documentFile: File): Response<SuccessMessage> {
        val map = mapOf(
            "role" to request.role,
            "email" to request.email,
            "password" to request.password,
            "confirmPassword" to request.confirmPassword,
            "firstName" to request.firstName,
            "lastName" to request.lastName,
            "dob" to request.dob,
            "address" to request.address,
            "workplace" to request.workplace,
            "specialization" to request.specialization,
            "whatsappUrl" to request.whatsappUrl
        )

        val bodyMap = map.mapValues { RequestBody.create("text/plain".toMediaTypeOrNull(), it.value) }
        val documentPart = MultipartBody.Part.createFormData(
            "document",
            documentFile.name,
            RequestBody.create("application/pdf".toMediaTypeOrNull(), documentFile)
        )

        return apiService.doctorSignup(
            role = bodyMap["role"]!!,
            email = bodyMap["email"]!!,
            password = bodyMap["password"]!!,
            confirmPassword = bodyMap["confirmPassword"]!!,
            firstName = bodyMap["firstName"]!!,
            lastName = bodyMap["lastName"]!!,
            dob = bodyMap["dob"]!!,
            address = bodyMap["address"]!!,
            workplace = bodyMap["workplace"]!!,
            specialization = bodyMap["specialization"]!!,
            whatsappUrl = bodyMap["whatsappUrl"]!!,
            document = documentPart
        )
    }



    suspend fun getDetailUser(): Flow<Result<UserData>> = flow {
        try {
            val response = apiService.getUserDetail()
            if (response.isSuccessful) {
                // Pastikan body tidak null sebelum emit
                response.body()?.let { userResponse ->
                    userResponse.data?.let { userData ->
                        emit(Result.Success(userData))
                    } ?: emit(Result.Error("User detail is null"))
                } ?: emit(Result.Error("Response body is null"))
            } else {
                // Mengambil pesan error dari errorBody
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = parseErrorMessage(errorBody)
                emit(Result.Error("Error: $errorMessage"))
            }
        } catch (e: IOException) {
            // Menangani error koneksi
            emit(Result.Error("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            // Menangani error HTTP
            emit(Result.Error("HTTP error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            // Menangani error lainnya
            emit(Result.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }.catch { e ->
        // Tambahan error handling di luar try-catch
        emit(Result.Error("Flow error: ${e.localizedMessage}"))
    }.flowOn(Dispatchers.IO)


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

    fun sendMessage(message: String, callback: (kotlin.Result<ChatData>) -> Unit) {
        val requestBody = ChatRequest(message)
        apiService.sendMessage(requestBody).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback(kotlin.Result.success(response.body()!!.data))
                } else {
                    callback(kotlin.Result.failure(Exception("Response unsuccessful or empty")))
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                callback(kotlin.Result.failure(t))
            }
        })
    }



    suspend fun getArticles(): ArticleResponse {
        return try {
            val response = apiService.getArticles()
            Log.d("ArticleRepository", "Success: $response")
            response
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            Log.e("ArticleRepository", "Error: ${e.message}")
            throw Exception(errorBody.message)
        }
    }

    suspend fun createArticle(
        title: RequestBody,
        content: RequestBody,
        image: MultipartBody.Part
    ): FileUploadResponse {
        return try {
            val response = apiService.createArticle(title, content, image)
            Log.d("ArticleAddRepository", "Success: $response")
            response
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            Log.e("ArticleAddRepository", "Error: ${e.message}")
            throw Exception(errorBody.message)
        }
    }

    suspend fun getArticleDetail(id: String): ArticleDetailResponse {
        return try {
            val response = apiService.getArticleDetail(id)
            Log.d("ArticleRepository", "Success: $response")
            response
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            Log.e("ArticleRepository", "Error: ${e.message}")
            throw Exception(errorBody.message)
        }
    }





}