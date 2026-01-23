package com.example.bia.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = FoodItem::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = MealGroup::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MealEntry (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val foodId: Int?,
    val groupId: Int,
    val quantity: Float,
    val timestamp: Instant,

    // only show if food item dies
    val caloriesSnapshot: Int,
    val nameSnapshot: String,
)
