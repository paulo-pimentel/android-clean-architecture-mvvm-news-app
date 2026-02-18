package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.config.EnvConfig
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.constants.ApiConstants
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.api.NewsApi
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticleDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for remote article data source.
 *
 * Defines the contract for fetching articles from the NewsAPI.
 */
interface ArticleRemoteDataSource {

    /**
     * Fetches top headlines from NewsAPI.
     *
     * @return List of [ArticleDto] from the API.
     * @throws NewsException.ApiKeyNotConfigured If API key is not set.
     * @throws NewsException.Server If the request fails or returns an error.
     */
    suspend fun getArticles(): List<ArticleDto>
}

/**
 * Implementation of [ArticleRemoteDataSource] using Retrofit.
 *
 * Fetches US business news from NewsAPI's top-headlines endpoint.
 *
 * @param newsApi The Retrofit API interface.
 */
@Singleton
class ArticleRemoteDataSourceImpl @Inject constructor(
    private val newsApi: NewsApi
) : ArticleRemoteDataSource {

    /**
     * Fetches top headlines from NewsAPI.
     *
     * Validates API key configuration before making the request.
     * Handles various API error responses appropriately.
     *
     * @return List of [ArticleDto] from the API.
     * @throws NewsException.ApiKeyNotConfigured If API key is not configured.
     * @throws NewsException.Server If the API request fails.
     */
    override suspend fun getArticles(): List<ArticleDto> {
        // Check API key configuration
        if (!EnvConfig.isApiKeyConfigured) {
            throw NewsException.ApiKeyNotConfigured()
        }

        return try {
            val response = newsApi.getTopHeadlines(
                apiKey = EnvConfig.newsApiKey,
                country = ApiConstants.COUNTRY,
                category = ApiConstants.CATEGORY
            )

            // Check for API key errors in response
            if (response.isApiKeyError) {
                throw NewsException.ApiKeyNotConfigured()
            }

            // Check for general API errors
            if (!response.isSuccess) {
                throw NewsException.Server(response.message ?: "Unknown API error")
            }

            response.articles ?: emptyList()
        } catch (e: NewsException) {
            // Re-throw our custom exceptions
            throw e
        } catch (e: Exception) {
            // Wrap other exceptions as server errors
            throw NewsException.Server(e.message)
        }
    }
}
