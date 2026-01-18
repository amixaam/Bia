package com.example.bia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bia.data.Converters
import com.example.bia.data.FoodItem
import com.example.bia.data.MealEntry

@Database(entities = [FoodItem::class, MealEntry::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao() : FoodDao
    abstract fun mealDao() : MealDao

    // Singleton block
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // If the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nutrition_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}