package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Utility object for date formatting operations.
 *
 * Provides methods to format dates in a user friendly format,
 * such as "Today", "Yesterday", or "X days ago".
 */
object DateFormatter {

    /**
     * Formats an [Instant] to a relative date string.
     *
     * @param instant The instant to format.
     * @return A user friendly relative date string:
     *         - "Today" if the date is today
     *         - "Yesterday" if the date is yesterday
     *         - "X days ago" for other cases
     */
    fun formatRelativeDate(instant: Instant): String {
        val today = LocalDate.now()
        val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        val daysDifference = ChronoUnit.DAYS.between(date, today).toInt()

        return when (daysDifference) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> "$daysDifference days ago"
        }
    }
}
