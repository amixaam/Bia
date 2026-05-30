package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bia.data.dataclass.Meal
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface MealDao {
    @Insert
    suspend fun createMeal(meal: Meal): Long

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Query("SELECT * FROM Meal WHERE id = :id")
    suspend fun getMealById(id: Int): Meal

    @Query("SELECT * FROM Meal WHERE timestamp >= :start AND timestamp <= :end")
    fun getMealsFromDateRange(start: Instant, end: Instant) : Flow<List<Meal>>

    @Query("SELECT * FROM Meal")
    suspend fun getAllMeals() : List<Meal>

    @Query("DELETE FROM Meal")
    suspend fun deleteAllMeals()

    @Query("DELETE FROM Meal WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun deleteAllMealsFromDateRange(start: Instant, end: Instant)

    @Query("UPDATE Meal SET pinned = :pinned WHERE id = :id")
    suspend fun setMealPin(id: Int, pinned: Boolean)

    @Query("SELECT * FROM Meal WHERE pinned = 1 ORDER BY timestamp DESC")
    fun getPinnedMeals(): Flow<List<Meal>>
}