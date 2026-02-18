package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.api

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.constants.ApiConstants.API_KEY_HEADER
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Retrofit interface for NewsAPI endpoints.
 *
 * Defines the API contract for fetching news articles.
 */
interface NewsApi {

    /**
     * Fetches top headlines from NewsAPI.
     *
     * @param apiKey The API key for authentication (passed via header).
     * @param country The country code to filter news (e.g., "us").
     * @param category The category to filter news (e.g., "business").
     * @return [ArticlesResponse] containing the list of articles.
     */
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Header(API_KEY_HEADER) apiKey: String,
        @Query("country") country: String,
        @Query("category") category: String
    ): ArticlesResponse
}
