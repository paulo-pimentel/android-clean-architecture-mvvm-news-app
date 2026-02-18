package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.util.DateFormatter
import java.time.Instant

/**
 * Composable displaying article publication date.
 *
 * Shows relative date format ("Today", "Yesterday", "X days ago").
 *
 * @param publishedAt The publication date of the article.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun ArticleDateLabel(
    publishedAt: Instant,
    modifier: Modifier = Modifier
) {
    Text(
        text = DateFormatter.formatRelativeDate(publishedAt),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ArticleDateLabelTodayPreview() {
    ArticleDateLabel(publishedAt = Instant.now())
}

@Preview(showBackground = true)
@Composable
private fun ArticleDateLabelYesterdayPreview() {
    ArticleDateLabel(publishedAt = Instant.now().minusSeconds(86400))
}
