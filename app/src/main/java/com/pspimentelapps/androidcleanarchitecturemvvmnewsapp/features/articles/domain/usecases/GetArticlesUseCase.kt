package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.usecases

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.repositories.ArticleRepository
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import javax.inject.Inject

/**
 * Use case for fetching news articles.
 *
 * Encapsulates the business logic for retrieving articles.
 * Following the single responsibility principle, this use case
 * only handles article fetching.
 *
 * @param repository The article repository for data access.
 */
class GetArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    /**
     * Executes the use case to fetch articles.
     *
     * Delegates to the repository and returns the result directly.
     *
     * @return [Result] containing list of articles on success,
     *         or an exception on failure.
     */
    suspend operator fun invoke(): Result<List<Article>> = repository.getArticles()
}