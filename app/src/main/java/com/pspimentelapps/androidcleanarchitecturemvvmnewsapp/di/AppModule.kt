package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.di

import android.content.Context
import android.content.SharedPreferences
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.network.NetworkInfo
import com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.network.NetworkInfoImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** SharedPreferences file name. */
private const val PREFS_NAME = "news_app_prefs"

/**
 * Hilt module providing core application dependencies.
 *
 * Provides singleton instances of SharedPreferences and NetworkInfo.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Binds [NetworkInfoImpl] to [NetworkInfo] interface.
     */
    @Binds
    @Singleton
    abstract fun bindNetworkInfo(networkInfoImpl: NetworkInfoImpl): NetworkInfo

    companion object {
        /**
         * Provides SharedPreferences instance for local data storage.
         *
         * @param context Application context.
         * @return SharedPreferences instance.
         */
        @Provides
        @Singleton
        fun provideSharedPreferences(
            @ApplicationContext context: Context
        ): SharedPreferences {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }
}
