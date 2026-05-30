package com.example.bia.data.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val pinned: Boolean,
    val timestamp: Instant
)
