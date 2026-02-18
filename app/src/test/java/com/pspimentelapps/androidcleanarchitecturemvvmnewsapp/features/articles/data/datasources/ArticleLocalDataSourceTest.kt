package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources

import android.content.SharedPreferences
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticleDto
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.SourceDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Unit tests for [ArticleLocalDataSourceImpl].
 */
@DisplayName("ArticleLocalDataSource")
class ArticleLocalDataSourceTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var moshi: Moshi
    private lateinit var dataSource: ArticleLocalDataSourceImpl

    private val testArticleDtos = listOf(
        ArticleDto(
            title = "Cached Article",
            description = "Cached Description",
            imageUrl = "https://example.com/cached.jpg",
            publishedAt = "2025-02-14T10:00:00Z",
            author = "Cached Author",
            url = "https://example.com/cached",
            source = SourceDto(name = "Cached Source")
        )
    )

    @BeforeEach
    fun setup() {
        sharedPreferences = mockk()
        editor = mockk(relaxed = true)
        moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.putLong(any(), any()) } returns editor
        
        dataSource = ArticleLocalDataSourceImpl(sharedPreferences, moshi)
    }

    @Nested
    @DisplayName("getLastArticles")
    inner class GetLastArticles {

        @Test
        @DisplayName("should return cached articles when data exists")
        fun `returns cached articles`() = runTest {
            // Arrange
            val jsonString = """
                [{"title":"Cached Article","description":"Cached Description",
                "urlToImage":"https://example.com/cached.jpg",
                "publishedAt":"2025-02-14T10:00:00Z","author":"Cached Author",
                "url":"https://example.com/cached",
                "source":{"id":null,"name":"Cached Source"}}]
            """.trimIndent()
            
            every { sharedPreferences.getString(any(), any()) } returns jsonString

            // Act
            val result = dataSource.getLastArticles()

            // Assert
            assertEquals(1, result.size)
            assertEquals("Cached Article", result.first().title)
        }

        @Test
        @DisplayName("should throw CacheException when no cached data")
        fun `throws when no cache`() = runTest {
            // Arrange
            every { sharedPreferences.getString(any(), any()) } returns null

            // Act & Assert
            assertThrows<NewsException.Cache> {
                dataSource.getLastArticles()
            }
        }

        @Test
        @DisplayName("should throw CacheException when cached data is empty")
        fun `throws when cache empty`() = runTest {
            // Arrange
            every { sharedPreferences.getString(any(), any()) } returns ""

            // Act & Assert
            assertThrows<NewsException.Cache> {
                dataSource.getLastArticles()
            }
        }

        @Test
        @DisplayName("should throw CacheException when cached data is invalid JSON")
        fun `throws on invalid json`() = runTest {
            // Arrange
            every { sharedPreferences.getString(any(), any()) } returns "invalid json"

            // Act & Assert
            assertThrows<NewsException.Cache> {
                dataSource.getLastArticles()
            }
        }
    }

    @Nested
    @DisplayName("cacheArticles")
    inner class CacheArticles {

        @Test
        @DisplayName("should save articles as JSON to SharedPreferences")
        fun `caches articles`() = runTest {
            // Arrange
            val jsonSlot = slot<String>()
            every { editor.putString(eq("CACHED_ARTICLES"), capture(jsonSlot)) } returns editor

            // Act
            dataSource.cacheArticles(testArticleDtos)

            // Assert
            verify { editor.putString("CACHED_ARTICLES", any()) }
            verify { editor.putLong("CACHED_TIMESTAMP", any()) }
            verify { editor.apply() }
            
            val savedJson = jsonSlot.captured
            assertNotNull(savedJson)
            assert(savedJson.contains("Cached Article"))
        }

        @Test
        @DisplayName("should save current timestamp")
        fun `saves timestamp`() = runTest {
            // Arrange
            val timestampSlot = slot<Long>()
            every { editor.putLong(eq("CACHED_TIMESTAMP"), capture(timestampSlot)) } returns editor

            // Act
            val beforeTime = System.currentTimeMillis()
            dataSource.cacheArticles(testArticleDtos)
            val afterTime = System.currentTimeMillis()

            // Assert
            verify { editor.putLong("CACHED_TIMESTAMP", any()) }
            
            val savedTimestamp = timestampSlot.captured
            assert(savedTimestamp in beforeTime..afterTime)
        }
    }

    @Nested
    @DisplayName("getCachedTimestamp")
    inner class GetCachedTimestamp {

        @Test
        @DisplayName("should return timestamp when exists")
        fun `returns timestamp`() = runTest {
            // Arrange
            val expectedTimestamp = 1707904800000L
            every { sharedPreferences.getLong(eq("CACHED_TIMESTAMP"), any()) } returns expectedTimestamp

            // Act
            val result = dataSource.getCachedTimestamp()

            // Assert
            assertEquals(expectedTimestamp, result)
        }

        @Test
        @DisplayName("should return null when no timestamp")
        fun `returns null when no timestamp`() = runTest {
            // Arrange
            every { sharedPreferences.getLong(eq("CACHED_TIMESTAMP"), eq(-1L)) } returns -1L

            // Act
            val result = dataSource.getCachedTimestamp()

            // Assert
            assertNull(result)
        }
    }
}
