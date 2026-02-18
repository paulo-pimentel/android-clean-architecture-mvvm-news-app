package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Unit tests for [DateFormatter].
 */
@DisplayName("DateFormatter")
class DateFormatterTest {

    @Nested
    @DisplayName("formatRelativeDate")
    inner class FormatRelativeDate {

        @Test
        @DisplayName("should return 'Today' for today's date")
        fun `returns Today for current date`() {
            // Arrange
            val today = Instant.now()

            // Act
            val result = DateFormatter.formatRelativeDate(today)

            // Assert
            assertEquals("Today", result)
        }

        @Test
        @DisplayName("should return 'Yesterday' for yesterday's date")
        fun `returns Yesterday for one day ago`() {
            // Arrange
            val yesterday = Instant.now().minus(1, ChronoUnit.DAYS)

            // Act
            val result = DateFormatter.formatRelativeDate(yesterday)

            // Assert
            assertEquals("Yesterday", result)
        }

        @Test
        @DisplayName("should return '2 days ago' for two days old date")
        fun `returns X days ago for older dates`() {
            // Arrange
            val twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS)

            // Act
            val result = DateFormatter.formatRelativeDate(twoDaysAgo)

            // Assert
            assertEquals("2 days ago", result)
        }

        @Test
        @DisplayName("should return '7 days ago' for a week old date")
        fun `returns correct days for a week old`() {
            // Arrange
            val weekAgo = Instant.now().minus(7, ChronoUnit.DAYS)

            // Act
            val result = DateFormatter.formatRelativeDate(weekAgo)

            // Assert
            assertEquals("7 days ago", result)
        }

        @Test
        @DisplayName("should return '30 days ago' for a month old date")
        fun `returns correct days for a month old`() {
            // Arrange
            val monthAgo = Instant.now().minus(30, ChronoUnit.DAYS)

            // Act
            val result = DateFormatter.formatRelativeDate(monthAgo)

            // Assert
            assertEquals("30 days ago", result)
        }
    }
}
