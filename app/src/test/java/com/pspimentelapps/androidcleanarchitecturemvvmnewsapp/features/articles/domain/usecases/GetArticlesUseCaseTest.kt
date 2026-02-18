package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.usecases

import coil3.util.CoilUtils.result
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.repositories.ArticleRepository
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

/**
 * Unit tests for [GetArticlesUseCase].
 */
@DisplayName("GetArticlesUseCase")
class GetArticlesUseCaseTest {

    private lateinit var repository: ArticleRepository
    private lateinit var useCase: GetArticlesUseCase

    private val testArticles = listOf(
        Article(
            title = "Test Article",
            description = "Test Description",
            imageUrl = "https://example.com/image.jpg",
            publishedAt = Instant.parse("2025-02-14T10:00:00Z"),
            author = "Test Author",
            url = "https://example.com/article",
            sourceName = "Test Source"
        )
    )

    @BeforeEach
    fun setup() {
        repository = mockk()
        useCase = GetArticlesUseCase(repository)
    }

    @Nested
    @DisplayName("invoke")
    inner class Invoke {

        @Test
        @DisplayName("should return articles from repository on success")
        fun `returns articles on success`() = runTest {
            // Arrange
            coEvery { repository.getArticles() } returns Result.success(testArticles)

            // Act
            val result = useCase()

            // Assert
            assertTrue(result.isSuccess)
            assertEquals(testArticles, result.getOrNull())
            coVerify(exactly = 1) { repository.getArticles() }
        }

        @Test
        @DisplayName("should return ServerException failure from repository")
        fun `returns server failure`() = runTest {
            // Arrange
            val exception = NewsException.Server("Server error")
            coEvery { repository.getArticles() } returns Result.failure(exception)

            // Act
            val result = useCase()

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NewsException.Server)
            coVerify(exactly = 1) { repository.getArticles() }
        }

        @Test
        @DisplayName("should return CacheException failure from repository")
        fun `returns cache failure`() = runTest {
            // Arrange
            coEvery { repository.getArticles() } returns Result.failure(NewsException.Cache())

            // Act
            val result = useCase()

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NewsException.Cache)
            coVerify(exactly = 1) { repository.getArticles() }
        }

        @Test
        @DisplayName("should return ApiKeyNotConfigured failure from repository")
        fun `returns api key failure`() = runTest {
            // Arrange
            coEvery { 
                repository.getArticles() 
            } returns Result.failure(NewsException.ApiKeyNotConfigured())

            // Act
            val result = useCase()

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NewsException.ApiKeyNotConfigured)
            coVerify(exactly = 1) { repository.getArticles() }
        }
    }
}
