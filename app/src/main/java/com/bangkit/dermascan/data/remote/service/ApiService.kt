package com.bangkit.dermascan.data.remote.service

import com.bangkit.dermascan.data.model.response.message.SuccessMessage
import com.bangkit.dermascan.data.model.response.message.chat.ChatRequest
import com.bangkit.dermascan.data.model.response.message.chat.ChatResponse
import com.bangkit.dermascan.data.model.requestBody.AuthRequest
import com.bangkit.dermascan.data.model.requestBody.ForumRequest
import com.bangkit.dermascan.data.model.requestBody.UserRequest
import com.bangkit.dermascan.data.model.response.ArticleDetailResponse
import com.bangkit.dermascan.data.model.response.ArticleResponse
//import com.bangkit.dermascan.data.model.response.ArticlesResponse
import com.bangkit.dermascan.data.model.response.BaseResponse
import com.bangkit.dermascan.data.model.response.FileUploadResponse
//import com.bangkit.dermascan.data.model.response.DataArticles
import com.bangkit.dermascan.data.model.response.ForumCreatedResponse
import com.bangkit.dermascan.data.model.response.ForumData
import com.bangkit.dermascan.data.model.response.ForumResponse
import com.bangkit.dermascan.data.model.response.LoginRequest
import com.bangkit.dermascan.data.model.response.LoginResponse
//import com.bangkit.dermascan.data.model.response.SkinLesion
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.data.model.response.SkinLesionsResponse
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.data.model.response.UserResponse
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
        @Part("whatsappUrl") whatsappUrl: RequestBody,
        @Part document: MultipartBody.Part
    ): Response<SuccessMessage>

    //USERS
    @GET("users/me")
    suspend fun getUserDetail(): Response<UserResponse>

    @PATCH("users/me")
    suspend fun updateDataUser(@Body userRequest: UserRequest): Response<BaseResponse<UserData>>

    @DELETE("users/me")
    suspend fun deleteUser(): UserResponse

    @GET("users")
    suspend fun getAllUsers(
        @Query("page") page: Int ?= 1,
        @Query("size") size: Int ?= 10,
        @Query("role") role: String? = null,

        ): Response<UserResponse>


    //ARTICLES
//    @POST("articles")
//    suspend fun createArticle(@Body forumRequest: ForumRequest): Response<BaseResponse<ForumCreatedResponse>>
//
//    @GET("articles")
//    suspend fun getAllArticles(
//        @Query("page") page: Int ?= 1,
//        @Query("size") size: Int ?= 10,
//        @Query("role") role: String? = null
//    ): Response<ArticlesResponse>
//
//    @GET("articles/{articleId}")
//    suspend fun getArticleDetail(
//        @Path("articleId") articleId: String
//    ): Response<BaseResponse<DataArticles>>
//
//    @DELETE("articles/{articleId}")
//    suspend fun deleteArticle(
//        @Path("articleId") articleId: String
//    ): Response<BaseResponse<ArticlesResponse>>


    @GET("articles")
    suspend fun getArticles(): ArticleResponse

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

    //FORUMS
    @POST("forums")
    suspend fun createForum(@Body forumRequest: ForumRequest): Response<BaseResponse<ForumCreatedResponse>>

    @GET("forums")
    suspend fun getAllForums(
        @Query("page") page: Int ?= 1,
        @Query("size") size: Int ?= 10,
        @Query("role") role: String? = null
    ): Response<ForumResponse>

    @GET("forums/{articleId}")
    suspend fun getForumDetail(
        @Path("articleId") articleId: String
    ): Response<BaseResponse<ForumData>>

    @PATCH("forums/{articleId}")
    suspend fun updateForum(
        @Path("articleId") articleId: String,
        @Body forumRequest: ForumRequest
    ): Response<BaseResponse<ForumData>>

    @DELETE("forums/{articleId}")
    suspend fun deleteForum(
        @Path("articleId") articleId: String
    ): Response<BaseResponse<ForumResponse>>



    //



    //


    //uploadSkinLesionImg
    @Multipart
    @POST("skin-lesions")
    suspend fun uploadSkinImage(
        @Part image: MultipartBody.Part
    ) : Response<BaseResponse<ResponseBody>>

    @GET("skin-lesions/my")
    suspend fun getSkinLesions(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
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