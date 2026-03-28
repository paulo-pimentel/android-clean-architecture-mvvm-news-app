package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.error

sealed class NewsException : Exception() {
    data class Server(override val message: String? = null) : NewsException()
    class Cache : NewsException()
    class ApiKeyNotConfigured : NewsException()
}
