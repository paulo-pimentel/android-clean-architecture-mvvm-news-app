package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the News App.
 *
 * Annotated with [HiltAndroidApp] to enable Hilt dependency injection.
 * This triggers Hilt's code generation, including a base class for the
 * application that serves as the application-level dependency container.
 */
@HiltAndroidApp
class NewsApplication : Application()