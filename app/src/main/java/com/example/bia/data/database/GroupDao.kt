package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bia.data.MealGroup
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface GroupDao {
    @Insert
    suspend fun createMealGroup(group: MealGroup): Long
    @Update
    suspend fun updateMealGroup(group: MealGroup)
    @Delete
    suspend fun deleteMealGroup(group: MealGroup)

    @Query("SELECT * FROM MealGroup WHERE id = :id")
    suspend fun getMealGroupById(id: Int): MealGroup

    @Query("SELECT * FROM MealGroup WHERE timestamp >= :start AND timestamp <= :end")
    fun getMealGroupsFromDateRange(start: Instant, end: Instant) : Flow<List<MealGroup>>

    @Query("SELECT * FROM MealGroup")
    suspend fun getAllMealGroups() : List<MealGroup>

    @Query("DELETE FROM MealGroup")
    suspend fun deleteAllGroups()

    @Query("DELETE FROM MealGroup WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun deleteAllGroupsFromDateRange(start: Instant, end: Instant)
}