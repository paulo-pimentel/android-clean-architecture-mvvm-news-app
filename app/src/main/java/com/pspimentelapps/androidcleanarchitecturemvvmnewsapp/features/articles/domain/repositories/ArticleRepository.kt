package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.repositories

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article

/**
 * Repository interface for article operations.
 *
 * Defines the contract between domain and data layers.
 * Uses Kotlin's [Result] type for error handling.
 */
interface ArticleRepository {

    /**
     * Fetches the list of news articles.
     *
     * The implementation should:
     * - When online: Fetch from remote API, cache result, return data
     * - When offline: Return cached data if available
     * - Handle API key configuration errors appropriately
     *
     * @return [Result.success] with list of articles on success,
     *         [Result.failure] with [NewsException] on error.
     */
    suspend fun getArticles(): Result<List<Article>>
}
