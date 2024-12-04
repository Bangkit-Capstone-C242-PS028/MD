package com.bangkit.dermascan.data.model.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @field:SerializedName("listArticle")
    val listArticle: List<ListArticleItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ListArticleItem(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("imageUrl")
    val imageUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("author")
    val author: String? = null
)
