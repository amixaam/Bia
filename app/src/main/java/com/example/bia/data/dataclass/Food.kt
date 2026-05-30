package com.example.bia.data.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bia.data.MeasureUnit
import java.time.Instant

@Entity
data class Food(
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
