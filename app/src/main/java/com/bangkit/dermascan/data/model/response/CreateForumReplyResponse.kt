package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName

data class CreateForumReplyResponse(
    @field:SerializedName("statusCode")
    val statusCode: Int? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("data")
    val data: CreateForumReplyData? = null
)

data class CreateForumReplyData(
    @field:SerializedName("replyId")
    val replyId: String? = null
)
