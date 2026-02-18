package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models

import com.squareup.moshi.JsonClass

/**
 * Response wrapper for NewsAPI articles endpoint.
 *
 * Represents the JSON response structure from the NewsAPI top-headlines endpoint.
 *
 * @param status The response status ("ok" on success, "error" on failure).
 * @param totalResults The total number of results available.
 * @param articles The list of article DTOs.
 * @param code Error code if status is "error" (e.g., "apiKeyInvalid").
 * @param message Error message if status is "error".
 */
@JsonClass(generateAdapter = true)
data class ArticlesResponse(
    val status: String,
    val totalResults: Int?,
    val articles: List<ArticleDto>?,
    val code: String? = null,
    val message: String? = null
) {
    /**
     * Checks if the API response indicates success.
     */
    val isSuccess: Boolean
        get() = status == "ok"

    /**
     * Checks if the error is related to API key issues.
     */
    val isApiKeyError: Boolean
        get() = code in listOf("apiKeyInvalid", "apiKeyDisabled", "apiKeyExhausted")
}
