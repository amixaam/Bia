package com.example.bia.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bia.data.dataclass.Serving
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface ServingDao {
    @Insert
    suspend fun createServing(serving: Serving)

    @Delete
    suspend fun deleteServing(serving: Serving)

    @Update
    suspend fun updateServing(serving: Serving)

    @Query("DELETE FROM Serving WHERE timestamp >= :start AND timestamp <= :end")
    suspend fun deleteAllServingsFromDateRange(start: Instant, end: Instant)

    @Query("DELETE FROM Serving")
    suspend fun deleteAllServings()

    @Query("SELECT * FROM Serving WHERE timestamp >= :start AND timestamp <= :end")
    fun getServingsFromDateRange(start: Instant, end: Instant) : Flow<List<Serving>>
}