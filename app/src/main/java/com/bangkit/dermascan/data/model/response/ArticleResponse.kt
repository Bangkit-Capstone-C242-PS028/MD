package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @field:SerializedName("statusCode")
    val statusCode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: ArticleData? = null
)

data class ArticleData(
    @field:SerializedName("articles")
    val articles: List<ArticleItem>? = null,

    @field:SerializedName("meta")
    val meta: Meta? = null
)

data class ArticleItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    val imageUrl: String? = null,

    val createdAt: String? = null,

)
