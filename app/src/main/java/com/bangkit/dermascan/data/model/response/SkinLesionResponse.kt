package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName

data class SkinLesionsResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SkinLesionsData
)

data class SkinLesionsData(
    @SerializedName("data") val lesions: List<SkinLesionItem>,
    @SerializedName("meta") val meta: Meta
)

data class SkinLesionItem(
    @SerializedName("id") val id: String,
    @SerializedName("patientUid") val patientUid: String,
    @SerializedName("originalImageUrl") val originalImageUrl: String,
    @SerializedName("processedImageUrl") val processedImageUrl: String,
    @SerializedName("classification") val classification: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("processedAt") val processedAt: String
)