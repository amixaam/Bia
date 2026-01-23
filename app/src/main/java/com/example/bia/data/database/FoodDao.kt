package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bia.data.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun saveFood(food: FoodItem)

    @Delete
    suspend fun deleteFood(food: FoodItem)

    @Query("SELECT * FROM FoodItem")
    fun getAllFood() : Flow<List<FoodItem>>

    @Query("DELETE FROM FoodItem")
    suspend fun deleteAllFood()
}