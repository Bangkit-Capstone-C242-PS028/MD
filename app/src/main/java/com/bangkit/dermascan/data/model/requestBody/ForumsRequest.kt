package com.bangkit.dermascan.data.model.requestBody

import com.google.gson.annotations.SerializedName

data class ForumRequest(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String
)