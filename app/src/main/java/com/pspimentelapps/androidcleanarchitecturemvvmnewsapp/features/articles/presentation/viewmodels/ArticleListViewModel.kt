package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error.NewsException
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.usecases.GetArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel for the article list screen.
 *
 * Manages UI state using StateFlow with the stateIn pattern.
 * Handles initial load and pull-to-refresh functionality.
 *
 * @param getArticlesUseCase Use case for fetching articles.
 */
@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase
) : ViewModel() {

    /**
     * Trigger for refreshing articles.
     *
     * Emits [Unit] to trigger a new fetch operation.
     * Uses replay=1 to ensure new collectors get the latest trigger.
     */
    private val refreshTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).apply {
        // Trigger initial load
        tryEmit(Unit)
    }

    /**
     * UI state exposed to the Compose UI.
     *
     * Transforms refresh triggers into UI states using the stateIn pattern.
     * Automatically handles subscription lifecycle with [SharingStarted.WhileSubscribed].
     *
     * The 5-second stop timeout allows the state to survive configuration changes
     * while properly cleaning up resources when no longer needed.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ArticleUiState> = refreshTrigger
        .flatMapLatest { fetchArticles() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArticleUiState.Loading
        )

    /**
     * Triggers a refresh of the article list.
     *
     * Called by pull-to-refresh or retry button.
     * Safe to call multiple times; rapid calls are coalesced.
     */
    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }

    /**
     * Fetches articles and emits appropriate UI states.
     *
     * @return Flow of [ArticleUiState] representing the fetch operation.
     */
    private fun fetchArticles(): Flow<ArticleUiState> = flow {
        emit(ArticleUiState.Loading)

        val result = getArticlesUseCase()

        val state = result
            .map { articles -> ArticleUiState.Success(articles) }
            .getOrElse { exception ->
                ArticleUiState.Error(
                    message = mapExceptionToMessage(exception),
                    isConfigError = exception is NewsException.ApiKeyNotConfigured
                )
            }

        emit(state)
    }

    /**
     * Maps exceptions to user-friendly error messages.
     *
     * @param exception The exception to map.
     * @return User-friendly error message string.
     */
    private fun mapExceptionToMessage(exception: Throwable): String {
        return when (exception) {
            is NewsException.Server ->
                "Failed to fetch articles. Please try again."

            is NewsException.Cache ->
                "No cached data available. Please connect to the internet."

            is NewsException.ApiKeyNotConfigured ->
                buildString {
                    appendLine("News API key is not configured.")
                    appendLine()
                    appendLine("Please add NEWS_API_KEY to local.properties.")
                    appendLine()
                    append("Get your free API key at: https://newsapi.org/register")
                }

            else ->
                "An unexpected error occurred."
        }
    }
}
