package com.example.bia.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

data class MealCategory(
    val name: String,
    val startHour: Int,
    val endHour: Int
)

val mealCategories = listOf(
    MealCategory("Breakfast", 5, 10),
    MealCategory("Lunch", 11, 16),
    MealCategory("Dinner", 17, 23),
    MealCategory("Midnight feast", 0, 4)
)