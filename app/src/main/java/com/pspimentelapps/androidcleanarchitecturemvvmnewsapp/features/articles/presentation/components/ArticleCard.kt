package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.domain.entities.Article
import java.time.Instant

/**
 * Card composable for displaying a single article.
 *
 * Shows:
 * - Article image (or "NO IMAGE" placeholder)
 * - Title
 * - Description
 * - Source name and publication date
 *
 * @param article The article to display.
 * @param onClick Callback when the card is clicked.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Article image
            ArticleImage(
                imageUrl = article.imageUrl
            )

            // Article content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Description
                if (article.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer: Source and date
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Source name
                    if (article.sourceName.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = article.sourceName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    // Date
                    ArticleDateLabel(publishedAt = article.publishedAt)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleCardPreview() {
    ArticleCard(
        article = Article(
            title = "Breaking: Major Tech Company Announces New Product",
            description = "A leading technology company has unveiled its latest innovation, set to revolutionize the industry with cutting-edge features.",
            imageUrl = "https://example.com/image.jpg",
            publishedAt = Instant.now(),
            author = "John Doe",
            url = "https://example.com/article",
            sourceName = "Tech News"
        ),
        onClick = {}
    )
}
