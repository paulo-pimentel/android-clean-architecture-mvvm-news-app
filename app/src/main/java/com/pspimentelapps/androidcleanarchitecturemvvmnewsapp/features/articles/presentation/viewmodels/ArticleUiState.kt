package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.viewmodels

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article

/**
 * Sealed interface representing all possible UI states for the article list.
 *
 * Using sealed interface enables exhaustive when expressions in Compose.
 */
sealed interface ArticleUiState {

    /**
     * Initial loading state.
     *
     * Displayed when articles are being fetched for the first time.
     */
    data object Loading : ArticleUiState

    /**
     * Success state with loaded articles.
     *
     * @param articles List of articles to display.
     */
    data class Success(val articles: List<Article>) : ArticleUiState

    /**
     * Error state when fetching fails.
     *
     * @param message User-friendly error message to display.
     * @param isConfigError `true` if this is an API key configuration error,
     *                         which determines whether to show the retry button.
     */
    data class Error(
        val message: String,
        val isConfigError: Boolean = false
    ) : ArticleUiState
}
