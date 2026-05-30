package com.example.bia.util

import java.time.Instant
import java.time.ZoneId

fun generateMealTitle(timestamp: Instant): String {
    val hour = timestamp.atZone(ZoneId.systemDefault()).hour

    return when (hour) {
        in 0..4 -> "Late Night"
        in 5..10 -> "Morning"
        in 11..13 -> "Midday"
        in 14..16 -> "Afternoon"
        in 17..20 -> "Evening"
        in 21..23 -> "Night"
        else -> "Meal"
    }
}
