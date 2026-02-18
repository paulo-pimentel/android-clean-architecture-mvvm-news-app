package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources

import android.content.SharedPreferences
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticleDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

/** Key for storing cached articles in SharedPreferences. */
private const val CACHED_ARTICLES_KEY = "CACHED_ARTICLES"

/** Key for storing cache timestamp in SharedPreferences. */
private const val CACHED_TIMESTAMP_KEY = "CACHED_TIMESTAMP"

/**
 * Interface for local article data source.
 *
 * Handles caching articles locally for offline access.
 */
interface ArticleLocalDataSource {

    /**
     * Gets the last cached articles.
     *
     * @return List of cached [ArticleDto].
     * @throws NewsException.Cache If no cached data exists or parsing fails.
     */
    suspend fun getLastArticles(): List<ArticleDto>

    /**
     * Caches articles with current timestamp.
     *
     * This will overwrite any previously cached articles.
     *
     * @param articles The list of articles to cache.
     */
    suspend fun cacheArticles(articles: List<ArticleDto>)

    /**
     * Gets the timestamp when articles were last cached.
     *
     * @return The cache timestamp in milliseconds, or `null` if no cache exists.
     */
    suspend fun getCachedTimestamp(): Long?
}

/**
 * Implementation of [ArticleLocalDataSource] using SharedPreferences.
 *
 * Stores articles as JSON string and timestamp as milliseconds since epoch.
 *
 * @param sharedPreferences SharedPreferences for data persistence.
 * @param moshi Moshi instance for JSON serialization.
 */
@Singleton
class ArticleLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi
) : ArticleLocalDataSource {

    /** Moshi adapter for List<ArticleDto> serialization. */
    private val articlesAdapter by lazy {
        val type = Types.newParameterizedType(List::class.java, ArticleDto::class.java)
        moshi.adapter<List<ArticleDto>>(type)
    }

    /**
     * Gets the last cached articles from SharedPreferences.
     *
     * @return List of cached [ArticleDto].
     * @throws NewsException.Cache If no cache exists or JSON parsing fails.
     */
    override suspend fun getLastArticles(): List<ArticleDto> {
        val jsonString = sharedPreferences.getString(CACHED_ARTICLES_KEY, null)

        if (jsonString.isNullOrEmpty()) {
            throw NewsException.Cache()
        }

        return try {
            articlesAdapter.fromJson(jsonString)
                ?: throw NewsException.Cache()
        } catch (e: NewsException.Cache) {
            throw e
        } catch (e: Exception) {
            throw NewsException.Cache()
        }
    }

    /**
     * Caches articles to SharedPreferences with current timestamp.
     *
     * @param articles The list of articles to cache.
     */
    override suspend fun cacheArticles(articles: List<ArticleDto>) {
        val jsonString = articlesAdapter.toJson(articles)

        sharedPreferences.edit {
            putString(CACHED_ARTICLES_KEY, jsonString)
                .putLong(CACHED_TIMESTAMP_KEY, System.currentTimeMillis())
        }
    }

    /**
     * Gets the timestamp when articles were last cached.
     *
     * @return The cache timestamp in milliseconds, or `null` if no cache exists.
     */
    override suspend fun getCachedTimestamp(): Long? {
        val timestamp = sharedPreferences.getLong(CACHED_TIMESTAMP_KEY, -1L)
        return if (timestamp == -1L) null else timestamp
    }
}
