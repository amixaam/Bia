package com.example.bia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bia.data.Converters
import com.example.bia.data.dataclass.Food
import com.example.bia.data.dataclass.Serving
import com.example.bia.data.dataclass.Meal
import com.example.bia.data.dataclass.ScannedProduct

@Database(entities = [Food::class, Serving::class, Meal::class, ScannedProduct::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao() : FoodDao
    abstract fun servingDao() : ServingDao

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
