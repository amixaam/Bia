package com.example.bia.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val brand: String?,

    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,

    val unit: MeasureUnit = MeasureUnit.G,
    val lastUsed: Instant
)
