package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bia.data.FoodItem

@Dao
interface FoodDao {
    @Insert
    suspend fun saveFood(food: FoodItem)

    @Query("SELECT * FROM FoodItem")
    suspend fun getAllFood() : List<FoodItem>
}