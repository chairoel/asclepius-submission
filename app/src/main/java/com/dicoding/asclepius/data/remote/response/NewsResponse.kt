package com.dicoding.asclepius.data.remote.response

import com.dicoding.asclepius.data.remote.dto.ArticleDto
import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticleDto>? = null,

	@field:SerializedName("status")
	val status: String? = null
)
