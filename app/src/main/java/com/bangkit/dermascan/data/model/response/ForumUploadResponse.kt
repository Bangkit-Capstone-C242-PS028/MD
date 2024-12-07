package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName


data class ForumUploadResponse(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ForumUploadData
)

data class ForumUploadData(
    @SerializedName("forumId") val forumId: String
)


