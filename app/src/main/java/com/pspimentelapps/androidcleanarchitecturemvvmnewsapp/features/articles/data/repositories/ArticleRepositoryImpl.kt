package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.repositories

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.network.NetworkInfo
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleLocalDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleRemoteDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.repositories.ArticleRepository
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepositoryImpl @Inject constructor(
    private val remoteDataSource: ArticleRemoteDataSource,
    private val localDataSource: ArticleLocalDataSource,
    private val networkInfo: NetworkInfo
) : ArticleRepository {

    override suspend fun getArticles(): Result<List<Article>> {
        return if (networkInfo.isConnected()) {
            fetchFromRemoteWithFallback()
        } else {
            fetchFromCache()
        }
    }

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

    private suspend fun fetchFromCache(): Result<List<Article>> {
        return runCatching {
            localDataSource.getLastArticles().map { it.toDomain() }
        }.recoverCatching {
            throw NewsException.Cache()
        }
    }
}
