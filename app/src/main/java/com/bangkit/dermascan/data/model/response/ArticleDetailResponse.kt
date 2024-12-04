package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName

data class ArticleDetailResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("article")
    val article: Article? = null
)

data class Article(
    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)