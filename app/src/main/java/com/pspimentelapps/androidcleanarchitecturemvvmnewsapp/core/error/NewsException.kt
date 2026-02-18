package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error

/**
 * Sealed class representing all possible errors in the news app.
 *
 * Using sealed class enables exhaustive when expressions and
 * provides type-safe error handling throughout the application.
 */
sealed class NewsException : Exception() {

    /**
     * Thrown when a server request fails due to network or API issues.
     *
     * @param message Optional error message from the server.
     */
    data class Server(override val message: String? = null) : NewsException()

    /**
     * Thrown when no cached data is available for offline access.
     */
    class Cache : NewsException()

    /**
     * Thrown when the NEWS_API_KEY is not configured in local.properties.
     */
    class ApiKeyNotConfigured : NewsException()
}
