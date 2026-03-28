package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArticlesResponse(
    val status: String,
    val totalResults: Int?,
    val articles: List<ArticleDto>?,
    val code: String? = null,
    val message: String? = null
) {
    val isSuccess: Boolean get() = status == "ok"
    val isApiKeyError: Boolean
        get() = code in listOf(
            "apiKeyInvalid",
            "apiKeyDisabled",
            "apiKeyExhausted"
        )
}
