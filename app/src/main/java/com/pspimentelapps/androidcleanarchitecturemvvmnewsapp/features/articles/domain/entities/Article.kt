package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities

import java.time.Instant

data class Article(
    val title: String,
    val description: String,
    val imageUrl: String,
    val publishedAt: Instant,
    val author: String,
    val url: String,
    val sourceName: String
) {
    val hasImage: Boolean get() = imageUrl.isNotEmpty()
    val hasValidUrl: Boolean get() = url.isNotEmpty()
}
