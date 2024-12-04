package com.bangkit.dermascan.data.model.response.message

import com.google.gson.annotations.SerializedName

data class SuccessMessage(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String
)

data class ErrorMessage(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: Any
)
