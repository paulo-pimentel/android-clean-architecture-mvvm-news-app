package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.viewmodels

import app.cash.turbine.test
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.usecases.GetArticlesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

/**
 * Unit tests for [ArticleListViewModel].
 *
 * Uses Turbine for Flow testing and MockK for mocking.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@DisplayName("ArticleListViewModel")
class ArticleListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getArticlesUseCase: GetArticlesUseCase

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
        Dispatchers.setMain(testDispatcher)
        getArticlesUseCase = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("initial state")
    inner class InitialState {

        @Test
        @DisplayName("should emit Loading then Success when fetch succeeds")
        fun `emits loading then success`() = runTest {
            // Arrange
            coEvery { getArticlesUseCase() } returns Result.success(testArticles)

            // Act
            val viewModel = ArticleListViewModel(getArticlesUseCase)

            // Assert
            viewModel.uiState.test {
                assertEquals(ArticleUiState.Loading, awaitItem())
                
                val successState = awaitItem()
                assertTrue(successState is ArticleUiState.Success)
                assertEquals(testArticles, (successState as ArticleUiState.Success).articles)
                
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("should emit Loading then Error when fetch fails with ServerException")
        fun `emits error on server failure`() = runTest {
            // Arrange
            coEvery { getArticlesUseCase() } returns Result.failure(NewsException.Server())

            // Act
            val viewModel = ArticleListViewModel(getArticlesUseCase)

            // Assert
            viewModel.uiState.test {
                assertEquals(ArticleUiState.Loading, awaitItem())
                
                val errorState = awaitItem()
                assertTrue(errorState is ArticleUiState.Error)
                assertFalse((errorState as ArticleUiState.Error).isConfigError)
                assertTrue(errorState.message.contains("Failed to fetch"))
                
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("should emit Error with isConfigError=true for ApiKeyNotConfigured")
        fun `emits config error for api key issue`() = runTest {
            // Arrange
            coEvery { getArticlesUseCase() } returns Result.failure(NewsException.ApiKeyNotConfigured())

            // Act
            val viewModel = ArticleListViewModel(getArticlesUseCase)

            // Assert
            viewModel.uiState.test {
                assertEquals(ArticleUiState.Loading, awaitItem())
                
                val errorState = awaitItem()
                assertTrue(errorState is ArticleUiState.Error)
                assertTrue((errorState as ArticleUiState.Error).isConfigError)
                assertTrue(errorState.message.contains("API key"))
                
                cancelAndIgnoreRemainingEvents()
            }
        }

        @Test
        @DisplayName("should emit Error for CacheException")
        fun `emits cache error`() = runTest {
            // Arrange
            coEvery { getArticlesUseCase() } returns Result.failure(NewsException.Cache())

            // Act
            val viewModel = ArticleListViewModel(getArticlesUseCase)

            // Assert
            viewModel.uiState.test {
                assertEquals(ArticleUiState.Loading, awaitItem())
                
                val errorState = awaitItem()
                assertTrue(errorState is ArticleUiState.Error)
                assertFalse((errorState as ArticleUiState.Error).isConfigError)
                assertTrue(errorState.message.contains("No cached data"))
                
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Nested
    @DisplayName("refresh")
    inner class Refresh {

        @Test
        @DisplayName("should trigger new fetch when refresh is called")
        fun `triggers new fetch`() = runTest {
            // Arrange
            coEvery { getArticlesUseCase() } returns Result.success(testArticles)
            val viewModel = ArticleListViewModel(getArticlesUseCase)

            // Act & Assert
            viewModel.uiState.test {
                // Initial load
                skipItems(2) // Skip Loading and Success

                // Trigger refresh
                viewModel.refresh()
                
                assertEquals(ArticleUiState.Loading, awaitItem())
                assertTrue(awaitItem() is ArticleUiState.Success)
                
                cancelAndIgnoreRemainingEvents()
            }
            
            coVerify(exactly = 2) { getArticlesUseCase() }
        }

        @Test
        @DisplayName("should handle refresh failure correctly")
        fun `handles refresh failure`() = runTest {
            // Arrange
            coEvery { getArticlesUseCase() } returnsMany listOf(
                Result.success(testArticles),
                Result.failure(NewsException.Server())
            )
            val viewModel = ArticleListViewModel(getArticlesUseCase)

            // Act & Assert
            viewModel.uiState.test {
                // Initial load success
                skipItems(2) // Skip Loading and Success

                // Trigger refresh that fails
                viewModel.refresh()
                
                assertEquals(ArticleUiState.Loading, awaitItem())
                
                val errorState = awaitItem()
                assertTrue(errorState is ArticleUiState.Error)
                
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
