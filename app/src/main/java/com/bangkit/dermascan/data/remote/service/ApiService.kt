package com.bangkit.dermascan.data.remote.service

import android.util.Log
import com.bangkit.dermascan.data.model.response.message.SuccessMessage
import com.bangkit.dermascan.data.model.response.message.chat.ChatRequest
import com.bangkit.dermascan.data.model.response.message.chat.ChatResponse
import com.bangkit.dermascan.data.model.requestBody.AuthRequest
import com.bangkit.dermascan.data.model.requestBody.CreateForumReplyRequest
import com.bangkit.dermascan.data.model.requestBody.ForumRequest
import com.bangkit.dermascan.data.model.requestBody.UserRequest
import com.bangkit.dermascan.data.model.response.AddToFavoritesResponse
import com.bangkit.dermascan.data.model.response.ArticleDetailResponse
import com.bangkit.dermascan.data.model.response.ArticleResponse
//import com.bangkit.dermascan.data.model.response.ArticlesResponse
import com.bangkit.dermascan.data.model.response.BaseResponse
import com.bangkit.dermascan.data.model.response.CreateForumReplyResponse
import com.bangkit.dermascan.data.model.response.FileUploadResponse
//import com.bangkit.dermascan.data.model.response.DataArticles
//import com.bangkit.dermascan.data.model.response.ForumCreatedResponse
import com.bangkit.dermascan.data.model.response.ForumData
import com.bangkit.dermascan.data.model.response.ForumDetailResponse
import com.bangkit.dermascan.data.model.response.ForumRepliesResponse
import com.bangkit.dermascan.data.model.response.ForumResponse
import com.bangkit.dermascan.data.model.response.ForumUploadResponse
import com.bangkit.dermascan.data.model.response.GetDoctorResponse
import com.bangkit.dermascan.data.model.response.LoginRequest
import com.bangkit.dermascan.data.model.response.LoginResponse
import com.bangkit.dermascan.data.model.response.RemoveFromFavoritesResponse
//import com.bangkit.dermascan.data.model.response.SkinLesion
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.data.model.response.SkinLesionsResponse
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.data.model.response.UserResponse
import com.bangkit.dermascan.data.repository.ApiRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // patient
    @POST("auth/signup")
    suspend fun signup(@Body signupRequest: AuthRequest): Response<SuccessMessage>

    //doctor
    @Multipart
    @POST("auth/signup")
    suspend fun doctorSignup(
        @Part("role") role: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirmPassword: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("address") address: RequestBody,
        @Part("workplace") workplace: RequestBody,
        @Part("specialization") specialization: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part document: MultipartBody.Part
    ): Response<SuccessMessage>

    @Multipart
    @PATCH("users/me")
    suspend fun updateUser(
        @Part("firstName") firstName: RequestBody?=null,
        @Part("lastName") lastName: RequestBody?=null,
        @Part("address") address: RequestBody?=null,
        @Part image: MultipartBody.Part?=null // Gambar opsional
    ): Response<UserResponse>

    //USERS
    @GET("users/me")
    suspend fun getUserDetail(): Response<UserResponse>

    @DELETE("users/me")
    suspend fun deleteUser(): UserResponse

    @GET("users")
    suspend fun getAllDoctor(
        @Query("page") page: Int ?= 1,
        @Query("limit") size: Int ?= 3,
        @Query("role") role: String? = "DOCTOR",

        ): Response<GetDoctorResponse>


    @GET("articles")
    suspend fun getArticles(
        @Query("page") page: Int ?= 1,
        @Query("limit") limit: Int ?= 3
    ): ArticleResponse

    @GET("articles/{articleId}")
    suspend fun getArticleDetail(
        @Path("articleId") id: String
    ): ArticleDetailResponse

    @Multipart
    @POST("articles")
    suspend fun createArticle(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part
    ): FileUploadResponse

    @POST("articles/{articleId}/favorites")
    suspend fun addToFavorites(
        @Path("articleId") articleId: String
    ): AddToFavoritesResponse

    @DELETE("articles/{articleId}/favorites")
    suspend fun removeFromFavorites(
        @Path("articleId") articleId: String
    ): RemoveFromFavoritesResponse


    @POST("forums")
    suspend fun createForum(
        @Body forumRequest: ForumRequest
    ): ForumUploadResponse

    @GET("forums")
    suspend fun getForums(
        @Query("page") page: Int ?= 1,
        @Query("limit") limit: Int ?= 3
    ): ForumResponse

    @GET("forums/my")
    suspend fun getMyForums(): ForumResponse

    @GET("forums/{forumId}")
    suspend fun getForumDetail(
        @Path("forumId") forumId: String
    ): ForumDetailResponse

    @POST("forums/{forumId}/replies")
    suspend fun createForumReply(
        @Path("forumId") forumId: String,
        @Body request: CreateForumReplyRequest
    ): CreateForumReplyResponse

    @GET("forums/{forumId}/replies")
    suspend fun getForumReplies(
        @Path("forumId") forumId: String,
        @Query("page") page: Int ?= 1,
        @Query("limit") limit: Int ?= 3
    ): ForumRepliesResponse

    //uploadSkinLesionImg
    @Multipart
    @POST("skin-lesions")
    suspend fun uploadSkinImage(
        @Part image: MultipartBody.Part
    ) : Response<BaseResponse<ResponseBody>>

    @GET("skin-lesions/my")
    suspend fun getSkinLesions(
        @Query("page") page: Int ?= 1,
        @Query("limit") limit: Int ?= 3
    ): Response<SkinLesionsResponse>

    @GET("skin-lesions/{id}")
    suspend fun getSkinLesionDetail(
        @Path("id") id: String
    ): Response<SkinLesionItem>

    @DELETE("skin-lesions/{id}")
    suspend fun deleteSkinLesion(
        @Path("id") id: String
    ): Response<BaseResponse<SkinLesionsResponse>>


    //chat bot
    @POST("genai/chatbot")
    fun sendMessage(@Body requestBody: ChatRequest): Call<ChatResponse>
}