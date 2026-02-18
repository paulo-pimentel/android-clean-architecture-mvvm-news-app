package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.constants

/**
 * Constants for NewsAPI configuration.
 *
 * Centralizes all API-related configuration values
 * for easy maintenance and testing.
 */
object ApiConstants {

    /** Base URL for the NewsAPI. */
    const val BASE_URL = "https://newsapi.org/v2/"

    /** Country code for fetching news (US business news). */
    const val COUNTRY = "us"

    /** Category for fetching news. */
    const val CATEGORY = "business"

    /** HTTP header key for API authentication. */
    const val API_KEY_HEADER = "X-Api-Key"

    /** Connection timeout in seconds. */
    const val CONNECT_TIMEOUT_SECONDS = 30L

    /** Read timeout in seconds. */
    const val READ_TIMEOUT_SECONDS = 30L
}
