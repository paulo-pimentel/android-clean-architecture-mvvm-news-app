package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.R

/**
 * Composable for displaying article images with error handling.
 *
 * Shows a "NO IMAGE" placeholder when:
 * - Image URL is empty
 * - Image fails to load
 *
 * Uses Coil for efficient image loading and caching.
 *
 * @param imageUrl The URL of the image to display.
 * @param modifier Modifier for customizing the layout.
 * @param height The height of the image container.
 */
@Composable
fun ArticleImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    height: Dp = 200.dp
) {
    if (imageUrl.isEmpty()) {
        NoImagePlaceholder(height = height)
        return
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        contentScale = ContentScale.Crop,
        error = painterResource(id = android.R.drawable.ic_menu_gallery),
        placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
    )
}

/**
 * Placeholder composable displayed when image is unavailable.
 *
 * @param modifier Modifier for customizing the layout.
 * @param height The height of the placeholder.
 */
@Composable
private fun NoImagePlaceholder(
    modifier: Modifier = Modifier,
    height: Dp = 200.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_image),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleImagePreview() {
    ArticleImage(
        imageUrl = "https://example.com/image.jpg"
    )
}

@Preview(showBackground = true)
@Composable
private fun NoImagePlaceholderPreview() {
    NoImagePlaceholder()
}
