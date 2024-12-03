package com.bangkit.dermascan.data.model.requestBody

import com.google.gson.annotations.SerializedName

data class ArticlesRequestBody(

	@field:SerializedName("title")
	val title: String?= null,

	@field:SerializedName("content")
	val content: String?= null,
)
