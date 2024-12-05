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

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("avatar")
    val avatar: Any? = null
)
