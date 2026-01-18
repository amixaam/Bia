package com.example.bia.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class MealEntry (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val foodId: Int,
    val quantity: Float,
    val timestamp: Instant,

    // only show if food item dies
    val caloriesSnapshot: Int,
    val nameSnapshot: String,
)
