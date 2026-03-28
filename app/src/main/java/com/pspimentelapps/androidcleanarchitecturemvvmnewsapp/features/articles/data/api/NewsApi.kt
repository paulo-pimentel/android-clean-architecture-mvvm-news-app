package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.api

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.constants.ApiConstants.API_KEY_HEADER
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("country") country: String,
        @Query("category") category: String
    ): ArticlesResponse
}
