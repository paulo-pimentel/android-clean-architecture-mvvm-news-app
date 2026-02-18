package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable displaying a scrollable list of articles with pull-to-refresh.
 *
 * @param articles The list of articles to display.
 * @param onArticleClick Callback when an article is clicked.
 * @param onRefresh Callback when pull-to-refresh is triggered.
 * @param modifier Modifier for customizing the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                onRefresh()
                // Give a small delay for visual feedback
                delay(500)
                isRefreshing = false
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = articles,
                key = { article -> article.url }
            ) { article ->
                ArticleCard(
                    article = article,
                    onClick = { onArticleClick(article) }
                )
            }
        }
    }
}
