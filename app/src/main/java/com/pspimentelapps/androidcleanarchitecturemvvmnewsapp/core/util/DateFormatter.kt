package com.pspimentelapps.androidcleanarchitecturemvvmnewsapp.core.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object DateFormatter {

    fun formatRelativeDate(instant: Instant): String {
        val today = LocalDate.now()
        val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        return when (val daysDifference = ChronoUnit.DAYS.between(date, today).toInt()) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> "$daysDifference days ago"
        }
    }
}
