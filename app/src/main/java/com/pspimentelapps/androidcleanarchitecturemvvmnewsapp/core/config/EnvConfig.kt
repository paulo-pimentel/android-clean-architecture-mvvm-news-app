package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.config

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.BuildConfig

/**
 * Configuration object for environment variables.
 *
 * Provides access to API keys and other configuration values
 * loaded from BuildConfig (originally from local.properties).
 *
 * ## Setup
 *
 * 1. Create or edit `local.properties` in the project root:
 *    ```properties
 *    NEWS_API_KEY=your_api_key_here
 *    ```
 *
 * 2. The file is git-ignored by default to keep your API key private.
 *
 * ## Getting an API Key
 *
 * Register for a free API key at: https://newsapi.org/register
 */
object EnvConfig {

    /**
     * NewsAPI key loaded from BuildConfig.
     *
     * @return The API key string, or empty string if not configured.
     */
    val newsApiKey: String
        get() = BuildConfig.NEWS_API_KEY

    /**
     * Checks if the API key is properly configured.
     *
     * @return `true` if the API key is configured and not empty.
     */
    val isApiKeyConfigured: Boolean
        get() = newsApiKey.isNotBlank()
}
