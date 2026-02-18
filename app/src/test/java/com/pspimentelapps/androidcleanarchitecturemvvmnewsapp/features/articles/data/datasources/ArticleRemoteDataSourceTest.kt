package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.config.EnvConfig
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.api.NewsApi
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticleDto
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticlesResponse
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.SourceDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Unit tests for [ArticleRemoteDataSourceImpl].
 */
@DisplayName("ArticleRemoteDataSource")
class ArticleRemoteDataSourceTest {

    private lateinit var newsApi: NewsApi
    private lateinit var dataSource: ArticleRemoteDataSourceImpl

    private val testArticleDtos = listOf(
        ArticleDto(
            title = "Test Article",
            description = "Test Description",
            imageUrl = "https://example.com/image.jpg",
            publishedAt = "2025-02-14T10:00:00Z",
            author = "Test Author",
            url = "https://example.com/article",
            source = SourceDto(name = "Test Source")
        )
    )

    private val successResponse = ArticlesResponse(
        status = "ok",
        totalResults = 1,
        articles = testArticleDtos
    )

    @BeforeEach
    fun setup() {
        newsApi = mockk()
        mockkObject(EnvConfig)
        dataSource = ArticleRemoteDataSourceImpl(newsApi)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(EnvConfig)
    }

    @Nested
    @DisplayName("getArticles")
    inner class GetArticles {

        @Test
        @DisplayName("should throw ApiKeyNotConfigured when API key is not set")
        fun `throws when api key not configured`() = runTest {
            // Arrange
            every { EnvConfig.isApiKeyConfigured } returns false

            // Act & Assert
            assertThrows<NewsException.ApiKeyNotConfigured> {
                dataSource.getArticles()
            }
        }

        @Test
        @DisplayName("should return articles when API call succeeds")
        fun `returns articles on success`() = runTest {
            // Arrange
            every { EnvConfig.isApiKeyConfigured } returns true
            every { EnvConfig.newsApiKey } returns "test_api_key"
            coEvery { 
                newsApi.getTopHeadlines(any(), any(), any()) 
            } returns successResponse

            // Act
            val result = dataSource.getArticles()

            // Assert
            assertEquals(1, result.size)
            assertEquals("Test Article", result.first().title)
        }

        @Test
        @DisplayName("should throw ServerException when API returns error status")
        fun `throws server error on api error`() = runTest {
            // Arrange
            val errorResponse = ArticlesResponse(
                status = "error",
                totalResults = null,
                articles = null,
                message = "API Error"
            )
            
            every { EnvConfig.isApiKeyConfigured } returns true
            every { EnvConfig.newsApiKey } returns "test_api_key"
            coEvery { 
                newsApi.getTopHeadlines(any(), any(), any()) 
            } returns errorResponse

            // Act & Assert
            val exception = assertThrows<NewsException.Server> {
                dataSource.getArticles()
            }
            assertTrue(exception.message?.contains("API Error") == true)
        }

        @Test
        @DisplayName("should throw ApiKeyNotConfigured when API returns apiKeyInvalid")
        fun `throws api key error on invalid key response`() = runTest {
            // Arrange
            val errorResponse = ArticlesResponse(
                status = "error",
                totalResults = null,
                articles = null,
                code = "apiKeyInvalid"
            )
            
            every { EnvConfig.isApiKeyConfigured } returns true
            every { EnvConfig.newsApiKey } returns "test_api_key"
            coEvery { 
                newsApi.getTopHeadlines(any(), any(), any()) 
            } returns errorResponse

            // Act & Assert
            assertThrows<NewsException.ApiKeyNotConfigured> {
                dataSource.getArticles()
            }
        }

        @Test
        @DisplayName("should return empty list when articles is null")
        fun `returns empty list when no articles`() = runTest {
            // Arrange
            val emptyResponse = ArticlesResponse(
                status = "ok",
                totalResults = 0,
                articles = null
            )
            
            every { EnvConfig.isApiKeyConfigured } returns true
            every { EnvConfig.newsApiKey } returns "test_api_key"
            coEvery { 
                newsApi.getTopHeadlines(any(), any(), any()) 
            } returns emptyResponse

            // Act
            val result = dataSource.getArticles()

            // Assert
            assertTrue(result.isEmpty())
        }

        @Test
        @DisplayName("should wrap network exceptions as ServerException")
        fun `wraps network errors`() = runTest {
            // Arrange
            every { EnvConfig.isApiKeyConfigured } returns true
            every { EnvConfig.newsApiKey } returns "test_api_key"
            coEvery { 
                newsApi.getTopHeadlines(any(), any(), any()) 
            } throws RuntimeException("Network error")

            // Act & Assert
            val exception = assertThrows<NewsException.Server> {
                dataSource.getArticles()
            }
            assertTrue(exception.message?.contains("Network error") == true)
        }
    }
}
