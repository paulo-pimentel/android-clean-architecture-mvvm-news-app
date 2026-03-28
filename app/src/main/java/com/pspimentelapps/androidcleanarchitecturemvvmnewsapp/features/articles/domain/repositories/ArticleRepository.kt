package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.repositories

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article

interface ArticleRepository {
    suspend fun getArticles(): Result<List<Article>>
}
