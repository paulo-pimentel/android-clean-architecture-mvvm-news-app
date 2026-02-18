package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.R

/**
 * Composable displaying an error message with optional retry button.
 *
 * Shows a user-friendly error message with an appropriate icon.
 * The retry button is hidden for configuration errors (API key issues).
 *
 * @param message The error message to display.
 * @param isConfigError `true` if this is a configuration error.
 * @param onRetry Callback when retry button is clicked.
 * @param modifier Modifier for customizing the layout.
 */
@Composable
fun ErrorMessage(
    message: String,
    isConfigError: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Error icon
        Icon(
            imageVector = if (isConfigError) {
                Icons.Default.Lock
            } else {
                Icons.Default.Warning
            },
            contentDescription = null,
            modifier = Modifier.height(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error message
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        // Only show retry button for non-configuration errors
        if (!isConfigError) {
            Spacer(modifier = Modifier.height(24.dp))

            FilledTonalButton(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorMessagePreview() {
    ErrorMessage(
        message = "Failed to fetch articles. Please try again.",
        isConfigError = false,
        onRetry = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ErrorMessageConfigErrorPreview() {
    ErrorMessage(
        message = "News API key is not configured.\n\nPlease add NEWS_API_KEY to local.properties.",
        isConfigError = true,
        onRetry = {}
    )
}
