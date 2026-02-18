package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

/**
 * Data Transfer Object for article data from NewsAPI.
 *
 * Handles JSON serialization/deserialization using Moshi.
 * Provides conversion to domain [Article] entity.
 *
 * @param title The article title.
 * @param description The article description.
 * @param imageUrl The URL to the article image (mapped from urlToImage).
 * @param publishedAt The publication date as ISO 8601 string.
 * @param author The article author.
 * @param url The URL to the full article.
 * @param source The source information.
 */
@JsonClass(generateAdapter = true)
data class ArticleDto(
    val title: String?,
    val description: String?,
    @Json(name = "urlToImage") val imageUrl: String?,
    val publishedAt: String?,
    val author: String?,
    val url: String?,
    val source: SourceDto?
) {
    /**
     * Converts this DTO to a domain [Article] entity.
     *
     * Handles null values by providing sensible defaults:
     * - String fields default to empty string
     * - Dates default to Unix epoch (1970-01-01) if invalid
     *
     * @return Domain [Article] entity.
     */
    fun toDomain(): Article = Article(
        title = title.orEmpty(),
        description = description.orEmpty(),
        imageUrl = imageUrl.orEmpty(),
        publishedAt = parseDateTime(publishedAt),
        author = author.orEmpty(),
        url = url.orEmpty(),
        sourceName = source?.name.orEmpty()
    )

    companion object {
        /**
         * Creates an [ArticleDto] from a domain [Article].
         *
         * Used for caching articles locally.
         *
         * @param article The domain article to convert.
         * @return [ArticleDto] for serialization.
         */
        fun fromDomain(article: Article): ArticleDto = ArticleDto(
            title = article.title,
            description = article.description,
            imageUrl = article.imageUrl,
            publishedAt = article.publishedAt.toString(),
            author = article.author,
            url = article.url,
            source = SourceDto(name = article.sourceName)
        )

        /**
         * Parses an ISO 8601 date string to [Instant].
         *
         * @param dateString The date string to parse, may be null or invalid.
         * @return Parsed [Instant], or [Instant.EPOCH] if parsing fails.
         */
        private fun parseDateTime(dateString: String?): Instant {
            if (dateString.isNullOrEmpty()) return Instant.EPOCH
            return runCatching { Instant.parse(dateString) }.getOrDefault(Instant.EPOCH)
        }
    }
}

/**
 * DTO for news source information.
 *
 * @param id The source identifier (may be null).
 * @param name The display name of the source.
 */
@JsonClass(generateAdapter = true)
data class SourceDto(
    val id: String? = null,
    val name: String?
)
