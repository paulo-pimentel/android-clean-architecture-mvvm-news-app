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

@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val getArticlesUseCase: GetArticlesUseCase
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    ).apply {
        // Trigger initial load
        tryEmit(Unit)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ArticleUiState> = refreshTrigger
        .flatMapLatest { fetchArticles() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArticleUiState.Loading
        )

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }

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
