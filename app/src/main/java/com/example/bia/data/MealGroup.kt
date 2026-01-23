package com.example.bia.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class MealGroup(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val timestamp: Instant
)
