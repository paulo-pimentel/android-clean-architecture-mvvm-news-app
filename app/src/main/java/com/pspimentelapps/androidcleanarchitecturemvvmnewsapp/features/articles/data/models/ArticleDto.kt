package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

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
        fun fromDomain(article: Article): ArticleDto = ArticleDto(
            title = article.title,
            description = article.description,
            imageUrl = article.imageUrl,
            publishedAt = article.publishedAt.toString(),
            author = article.author,
            url = article.url,
            source = SourceDto(name = article.sourceName)
        )

        private fun parseDateTime(dateString: String?): Instant {
            if (dateString.isNullOrEmpty()) return Instant.EPOCH
            return runCatching { Instant.parse(dateString) }.getOrDefault(Instant.EPOCH)
        }
    }
}

@JsonClass(generateAdapter = true)
data class SourceDto(
    val id: String? = null,
    val name: String?
)
