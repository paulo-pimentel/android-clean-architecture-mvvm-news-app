package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Unit tests for [NetworkInfoImpl].
 */
@DisplayName("NetworkInfo")
class NetworkInfoTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkInfo: NetworkInfoImpl

    @BeforeEach
    fun setup() {
        context = mockk()
        connectivityManager = mockk()
        
        every { 
            context.getSystemService(Context.CONNECTIVITY_SERVICE) 
        } returns connectivityManager
        
        networkInfo = NetworkInfoImpl(context)
    }

    @Nested
    @DisplayName("isConnected")
    inner class IsConnected {

        @Test
        @DisplayName("should return true when device is connected with internet capability")
        fun `returns true when connected`() {
            // Arrange
            val network = mockk<Network>()
            val capabilities = mockk<NetworkCapabilities>()
            
            every { connectivityManager.activeNetwork } returns network
            every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
            every { 
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) 
            } returns true
            every { 
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) 
            } returns true

            // Act
            val result = networkInfo.isConnected()

            // Assert
            assertTrue(result)
            verify { connectivityManager.activeNetwork }
            verify { connectivityManager.getNetworkCapabilities(network) }
        }

        @Test
        @DisplayName("should return false when no active network")
        fun `returns false when no active network`() {
            // Arrange
            every { connectivityManager.activeNetwork } returns null

            // Act
            val result = networkInfo.isConnected()

            // Assert
            assertFalse(result)
        }

        @Test
        @DisplayName("should return false when network capabilities are null")
        fun `returns false when capabilities null`() {
            // Arrange
            val network = mockk<Network>()
            
            every { connectivityManager.activeNetwork } returns network
            every { connectivityManager.getNetworkCapabilities(network) } returns null

            // Act
            val result = networkInfo.isConnected()

            // Assert
            assertFalse(result)
        }

        @Test
        @DisplayName("should return false when no internet capability")
        fun `returns false when no internet capability`() {
            // Arrange
            val network = mockk<Network>()
            val capabilities = mockk<NetworkCapabilities>()
            
            every { connectivityManager.activeNetwork } returns network
            every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
            every { 
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) 
            } returns false

            // Act
            val result = networkInfo.isConnected()

            // Assert
            assertFalse(result)
        }

        @Test
        @DisplayName("should return false when network is not validated")
        fun `returns false when not validated`() {
            // Arrange
            val network = mockk<Network>()
            val capabilities = mockk<NetworkCapabilities>()
            
            every { connectivityManager.activeNetwork } returns network
            every { connectivityManager.getNetworkCapabilities(network) } returns capabilities
            every { 
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) 
            } returns true
            every { 
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) 
            } returns false

            // Act
            val result = networkInfo.isConnected()

            // Assert
            assertFalse(result)
        }
    }
}
