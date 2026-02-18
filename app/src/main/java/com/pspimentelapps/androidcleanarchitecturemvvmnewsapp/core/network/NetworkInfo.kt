package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for checking network connectivity.
 *
 * This abstraction allows for easy mocking in tests and
 * decouples the app from the specific connectivity implementation.
 */
interface NetworkInfo {

    /**
     * Checks if the device is currently connected to the internet.
     *
     * @return `true` if connected, `false` otherwise.
     */
    fun isConnected(): Boolean
}

/**
 * Implementation of [NetworkInfo] using Android's ConnectivityManager.
 *
 * Checks for active network connection with internet capability.
 *
 * @param context Application context for accessing system services.
 */
@Singleton
class NetworkInfoImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkInfo {

    /**
     * Checks if the device is currently connected to the internet.
     *
     * Uses [ConnectivityManager] to verify active network and internet capability.
     *
     * @return `true` if connected to internet, `false` otherwise.
     */
    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
