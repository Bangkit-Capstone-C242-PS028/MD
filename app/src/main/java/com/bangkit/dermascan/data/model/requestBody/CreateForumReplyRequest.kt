package com.bangkit.dermascan.data.model.requestBody

import com.google.gson.annotations.SerializedName

data class CreateForumReplyRequest(
    @field:SerializedName("content")
    val content: String
)
