package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.screens.ArticleDetailScreen
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.features.articles.presentation.screens.ArticleListScreen
import kotlinx.serialization.Serializable

/**
 * Type-safe route for the article list screen (home).
 */
@Serializable
data object ArticleListRoute

/**
 * Type-safe route for the article detail screen.
 *
 * @param url The URL of the article to display in WebView.
 * @param sourceName The name of the news source for the app bar.
 */
@Serializable
data class ArticleDetailRoute(
    val url: String,
    val sourceName: String
)

/**
 * Main navigation graph for the news app.
 *
 * Uses Navigation Compose with type-safe routes and Kotlin Serialization.
 *
 * @param modifier Modifier for the NavHost.
 */
@Composable
fun NewsNavGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ArticleListRoute,
        modifier = modifier
    ) {
        // Article list screen (home)
        composable<ArticleListRoute> {
            ArticleListScreen(
                onArticleClick = { article ->
                    navController.navigate(
                        ArticleDetailRoute(
                            url = article.url,
                            sourceName = article.sourceName
                        )
                    )
                }
            )
        }

        // Article detail screen (WebView)
        composable<ArticleDetailRoute> { backStackEntry ->
            val route: ArticleDetailRoute = backStackEntry.toRoute()

            ArticleDetailScreen(
                url = route.url,
                sourceName = route.sourceName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
