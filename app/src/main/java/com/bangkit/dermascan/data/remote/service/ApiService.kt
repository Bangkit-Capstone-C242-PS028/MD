package com.bangkit.dermascan.data.remote.service

import com.bangkit.dermascan.data.model.ApiResponse
import com.bangkit.dermascan.data.model.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/signup")
    fun signup(@Body signupRequest: SignUpRequest): Call<ApiResponse>
}