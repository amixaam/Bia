package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bia.data.MealGroup
import java.time.Instant

@Dao
interface GroupDao {
    @Insert
    suspend fun createMealGroup(group: MealGroup)
    @Update
    suspend fun updateMealGroup(group: MealGroup)
    @Delete
    suspend fun deleteMealGroup(group: MealGroup)

    @Query("SELECT * FROM MealGroup WHERE id = :id")
    suspend fun getMealGroupById(id: Int): MealGroup

    @Query("SELECT * FROM MealGroup WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun getMealGroupsFromDateRange(start: Instant, end: Instant) : List<MealGroup>

    @Query("SELECT * FROM MealGroup")
    suspend fun getAllMealGroups() : List<MealGroup>
}