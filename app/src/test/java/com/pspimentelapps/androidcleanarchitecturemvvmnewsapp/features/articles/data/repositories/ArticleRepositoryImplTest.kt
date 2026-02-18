package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.repositories

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.network.NetworkInfo
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleLocalDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.datasources.ArticleRemoteDataSource
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.ArticleDto
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.data.models.SourceDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Unit tests for [ArticleRepositoryImpl].
 */
@DisplayName("ArticleRepositoryImpl")
class ArticleRepositoryImplTest {

    private lateinit var remoteDataSource: ArticleRemoteDataSource
    private lateinit var localDataSource: ArticleLocalDataSource
    private lateinit var networkInfo: NetworkInfo
    private lateinit var repository: ArticleRepositoryImpl

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

    @BeforeEach
    fun setup() {
        remoteDataSource = mockk()
        localDataSource = mockk()
        networkInfo = mockk()
        repository = ArticleRepositoryImpl(remoteDataSource, localDataSource, networkInfo)
    }

    @Nested
    @DisplayName("getArticles - device is online")
    inner class DeviceOnline {

        @BeforeEach
        fun setupOnline() {
            every { networkInfo.isConnected() } returns true
        }

        @Test
        @DisplayName("should return remote data and cache it when successful")
        fun `returns remote data on success`() = runTest {
            // Arrange
            coEvery { remoteDataSource.getArticles() } returns testArticleDtos
            coEvery { localDataSource.cacheArticles(any()) } returns Unit

            // Act
            val result = repository.getArticles()

            // Assert
            assertTrue(result.isSuccess)
            assertEquals(1, result.getOrNull()?.size)
            assertEquals("Test Article", result.getOrNull()?.first()?.title)
            coVerify { remoteDataSource.getArticles() }
            coVerify { localDataSource.cacheArticles(testArticleDtos) }
        }

        @Test
        @DisplayName("should return cached data when remote fails with ServerException")
        fun `falls back to cache on server error`() = runTest {
            // Arrange
            coEvery { remoteDataSource.getArticles() } throws NewsException.Server()
            coEvery { localDataSource.getLastArticles() } returns testArticleDtos

            // Act
            val result = repository.getArticles()

            // Assert
            assertTrue(result.isSuccess)
            assertEquals(1, result.getOrNull()?.size)
            coVerify { remoteDataSource.getArticles() }
            coVerify { localDataSource.getLastArticles() }
        }

        @Test
        @DisplayName("should return ApiKeyNotConfigured without cache fallback")
        fun `does not fallback for api key error`() = runTest {
            // Arrange
            coEvery { remoteDataSource.getArticles() } throws NewsException.ApiKeyNotConfigured()

            // Act
            val result = repository.getArticles()

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NewsException.ApiKeyNotConfigured)
            coVerify { remoteDataSource.getArticles() }
            coVerify(exactly = 0) { localDataSource.getLastArticles() }
        }

        @Test
        @DisplayName("should return CacheFailure when remote fails and cache is empty")
        fun `returns cache failure when both fail`() = runTest {
            // Arrange
            coEvery { remoteDataSource.getArticles() } throws NewsException.Server()
            coEvery { localDataSource.getLastArticles() } throws NewsException.Cache()

            // Act
            val result = repository.getArticles()

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NewsException.Cache)
        }
    }

    @Nested
    @DisplayName("getArticles - device is offline")
    inner class DeviceOffline {

        @BeforeEach
        fun setupOffline() {
            every { networkInfo.isConnected() } returns false
        }

        @Test
        @DisplayName("should return cached data when offline")
        fun `returns cached data when offline`() = runTest {
            // Arrange
            coEvery { localDataSource.getLastArticles() } returns testArticleDtos

            // Act
            val result = repository.getArticles()

            // Assert
            assertTrue(result.isSuccess)
            assertEquals(1, result.getOrNull()?.size)
            coVerify(exactly = 0) { remoteDataSource.getArticles() }
            coVerify { localDataSource.getLastArticles() }
        }

        @Test
        @DisplayName("should return CacheFailure when offline and no cache")
        fun `returns cache failure when offline with no cache`() = runTest {
            // Arrange
            coEvery { localDataSource.getLastArticles() } throws NewsException.Cache()

            // Act
            val result = repository.getArticles()

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NewsException.Cache)
            coVerify(exactly = 0) { remoteDataSource.getArticles() }
        }
    }
}
