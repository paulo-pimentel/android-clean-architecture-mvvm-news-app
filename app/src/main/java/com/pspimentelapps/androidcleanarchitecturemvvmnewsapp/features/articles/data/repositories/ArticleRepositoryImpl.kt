package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.repositories

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.network.NetworkInfo
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleLocalDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleRemoteDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.repositories.ArticleRepository
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ArticleRepository].
 *
 * Handles the data fetching strategy:
 * - Online: Fetch from remote API, cache result, return data
 * - Online with error: Fall back to cached data
 * - Offline: Return cached data if available
 * - API key errors are returned immediately without fallback
 *
 * @param remoteDataSource Remote data source for API calls.
 * @param localDataSource Local data source for caching.
 * @param networkInfo Network connectivity checker.
 */
@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val remoteDataSource: ArticleRemoteDataSource,
    private val localDataSource: ArticleLocalDataSource,
    private val networkInfo: NetworkInfo
) : ArticleRepository {

    /**
     * Fetches articles with automatic caching and offline support.
     *
     * Strategy:
     * - If online: Try remote fetch, cache on success
     * - If remote fails (except API key error): Try cache
     * - If offline: Return cached data
     *
     * @return [Result.success] with articles, or [Result.failure] with exception.
     */
    override suspend fun getArticles(): Result<List<Article>> {
        return if (networkInfo.isConnected()) {
            fetchFromRemoteWithFallback()
        } else {
            fetchFromCache()
        }
    }

    /**
     * Fetches articles from remote API with cache fallback on failure.
     *
     * API key configuration errors are NOT retried from cache,
     * as this is a user configuration issue that needs to be addressed.
     *
     * @return [Result] with articles or failure.
     */
    private suspend fun fetchFromRemoteWithFallback(): Result<List<Article>> {
        return runCatching {
            val articles = remoteDataSource.getArticles()
            localDataSource.cacheArticles(articles)
            articles.map { it.toDomain() }
        }.recoverCatching { exception ->
            // Don't fall back to cache for API key errors
            if (exception is NewsException.ApiKeyNotConfigured) {
                throw exception
            }

            // Try cache as fallback for other errors
            localDataSource.getLastArticles().map { it.toDomain() }
        }
    }

    /**
     * Fetches articles from local cache.
     *
     * @return [Result] with cached articles or cache failure.
     */
    private suspend fun fetchFromCache(): Result<List<Article>> {
        return runCatching {
            localDataSource.getLastArticles().map { it.toDomain() }
        }.recoverCatching {
            throw NewsException.Cache()
        }
    }
}
