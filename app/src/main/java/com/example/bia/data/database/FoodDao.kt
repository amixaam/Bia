package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bia.data.dataclass.Food
import com.example.bia.data.dataclass.ScannedProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Insert
    suspend fun createFood(food: Food): Long

    @Delete
    suspend fun deleteFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Query("SELECT * FROM Food ORDER BY lastUsed DESC")
    fun getAllFood() : Flow<List<Food>>

    @Query("SELECT * FROM Food WHERE id = :id")
    suspend fun getFoodById(id: Int): Food?

    @Query("DELETE FROM Food")
    suspend fun deleteAllFood()

    //OFF (open food fact) products
    @Query(
        """
        SELECT Food.* FROM Food
        INNER JOIN ScannedProduct ON Food.id = ScannedProduct.foodId
        WHERE ScannedProduct.barcode = :barcode
    """
    )
    suspend fun getFoodByBarcode(barcode: String) : Food?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScannedProduct(scannedProduct: ScannedProduct)
}
