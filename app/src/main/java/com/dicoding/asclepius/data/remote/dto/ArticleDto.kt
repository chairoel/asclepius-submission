package com.dicoding.asclepius.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ArticleDto(

	@field:SerializedName("urlToImage")
	val urlToImage: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("url")
	val url: String? = null,
)

