package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName

data class ForumDetailResponse(
    @field:SerializedName("statusCode")
    val statusCode: Int? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("data")
    val data: ForumDetail? = null
)

data class ForumDetail(
    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("title")
    val title: String? = null,
    @field:SerializedName("content")
    val content: String? = null,
    @field:SerializedName("status")
    val status: String? = null,
    @field:SerializedName("created_at")
    val createdAt: String? = null,
    @field:SerializedName("patient")
    val patient: UserInfo? = null
)

data class ForumRepliesResponse(
    @field:SerializedName("statusCode")
    val statusCode: Int? = null,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("data")
    val data: ForumRepliesList
)

data class ForumRepliesList(
    @field:SerializedName("data")
    val data: List<ForumReply>,
    @field:SerializedName("meta")
    val meta: Meta? = null
)

data class ForumReply(
    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("responder_role")
    val responderRole: String? = null,
    @field:SerializedName("content")
    val content: String? = null,
    @field:SerializedName("created_at")
    val createdAt: String? = null,
    @field:SerializedName("responder")
    val responder: UserInfo? = null
)