package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bia.data.MealEntry
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface MealDao {
    @Insert
    suspend fun saveMeal(meal: MealEntry)

    @Delete
    suspend fun deleteMeal(meal: MealEntry)

    @Query("DELETE FROM MealEntry WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun deleteAllMealsFromDateRange(start: Instant, end: Instant)

    @Query("DELETE FROM MealEntry")
    suspend fun deleteAllMeals()

    @Query("SELECT * FROM MealEntry WHERE timestamp >= :start AND timestamp <= :end")
    fun getMealsFromDateRange(start: Instant, end: Instant) : Flow<List<MealEntry>>
}