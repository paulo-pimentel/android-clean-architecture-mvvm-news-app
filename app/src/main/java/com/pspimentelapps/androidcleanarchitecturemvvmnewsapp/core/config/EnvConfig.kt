package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.config

import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.BuildConfig

object EnvConfig {
    val newsApiKey get() = BuildConfig.NEWS_API_KEY
    val isApiKeyConfigured get() = newsApiKey.isNotBlank()
}
