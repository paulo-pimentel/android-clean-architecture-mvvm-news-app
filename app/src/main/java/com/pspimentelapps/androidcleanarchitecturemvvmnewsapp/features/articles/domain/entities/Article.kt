package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities

import java.time.Instant

/**
 * Domain entity representing a news article.
 *
 * This is the core business object used throughout the application.
 * It is independent of any data sources or external APIs.
 *
 * @param title The headline or title of the article.
 * @param description A brief description or snippet from the article.
 * @param imageUrl The URL to a relevant image for the article.
 * @param publishedAt The date and time when the article was published.
 * @param author The author of the article.
 * @param url The direct URL to the full article.
 * @param sourceName The name of the news source.
 */
data class Article(
    val title: String,
    val description: String,
    val imageUrl: String,
    val publishedAt: Instant,
    val author: String,
    val url: String,
    val sourceName: String
) {
    /**
     * Indicates whether the article has a valid image URL.
     */
    val hasImage: Boolean
        get() = imageUrl.isNotEmpty()

    /**
     * Indicates whether the article has a valid URL for viewing.
     */
    val hasValidUrl: Boolean
        get() = url.isNotEmpty()
}