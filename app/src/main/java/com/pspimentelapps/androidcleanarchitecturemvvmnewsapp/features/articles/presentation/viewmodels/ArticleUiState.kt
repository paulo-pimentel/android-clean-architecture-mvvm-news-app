package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.viewmodels

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article

sealed interface ArticleUiState {
    data object Loading : ArticleUiState
    data class Success(val articles: List<Article>) : ArticleUiState
    data class Error(
        val message: String,
        val isConfigError: Boolean = false
    ) : ArticleUiState
}
