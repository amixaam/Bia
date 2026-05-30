package com.example.bia.data.dataclass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["id"],
            childColumns = ["mealId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Serving (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val foodId: Int?,
    val mealId: Int,
    val quantity: Float, // quantity in g or ml
    val timestamp: Instant,

    // only show if food item dies
    val caloriesSnapshot: Int,
    val nameSnapshot: String,
)
