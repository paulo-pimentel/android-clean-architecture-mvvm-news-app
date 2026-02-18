package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.ui.theme.NewsAppTheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.navigation.NewsNavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point activity for the News App.
 *
 * Uses single-activity architecture with Compose navigation.
 * Annotated with [AndroidEntryPoint] to enable Hilt injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsNavGraph()
                }
            }
        }
    }
}