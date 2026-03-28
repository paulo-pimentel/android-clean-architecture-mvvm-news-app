package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components.ArticleList
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components.ErrorMessage
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components.LoadingIndicator
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.viewmodels.ArticleListViewModel
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.viewmodels.ArticleUiState
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.R
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    viewModel: ArticleListViewModel = hiltViewModel(),
    onArticleClick: (Article) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.business_news))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        when (val state = uiState) {
            is ArticleUiState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is ArticleUiState.Error -> {
                ErrorMessage(
                    message = state.message,
                    isConfigError = state.isConfigError,
                    onRetry = viewModel::refresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is ArticleUiState.Success -> {
                ArticleList(
                    articles = state.articles,
                    onArticleClick = { article ->
                        if (article.hasValidUrl) {
                            onArticleClick(article)
                        }
                    },
                    onRefresh = viewModel::refresh,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}
