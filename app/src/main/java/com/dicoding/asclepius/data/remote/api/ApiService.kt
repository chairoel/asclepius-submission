package com.dicoding.asclepius.data.remote.api

import com.dicoding.asclepius.data.remote.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("category") category: String,
        @Query("language") language: String
    ): Response<NewsResponse>
}
