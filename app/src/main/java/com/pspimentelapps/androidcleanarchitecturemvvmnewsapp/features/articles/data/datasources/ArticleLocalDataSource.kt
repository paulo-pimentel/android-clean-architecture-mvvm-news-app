package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources

import android.content.SharedPreferences
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticleDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

private const val CACHED_ARTICLES_KEY = "CACHED_ARTICLES"
private const val CACHED_TIMESTAMP_KEY = "CACHED_TIMESTAMP"

interface ArticleLocalDataSource {
    suspend fun getLastArticles(): List<ArticleDto>
    suspend fun cacheArticles(articles: List<ArticleDto>)
    suspend fun getCachedTimestamp(): Long?
}

@Singleton
class ArticleLocalDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val moshi: Moshi
) : ArticleLocalDataSource {

    private val articlesAdapter by lazy {
        val type = Types.newParameterizedType(List::class.java, ArticleDto::class.java)
        moshi.adapter<List<ArticleDto>>(type)
    }

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

    override suspend fun cacheArticles(articles: List<ArticleDto>) {
        val jsonString = articlesAdapter.toJson(articles)

        sharedPreferences.edit {
            putString(CACHED_ARTICLES_KEY, jsonString)
                .putLong(CACHED_TIMESTAMP_KEY, System.currentTimeMillis())
        }
    }

    override suspend fun getCachedTimestamp(): Long? {
        val timestamp = sharedPreferences.getLong(CACHED_TIMESTAMP_KEY, -1L)
        return if (timestamp == -1L) null else timestamp
    }
}
