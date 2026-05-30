package com.example.bia.data.dataclass

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Food::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index("foodId")]
)
data class ScannedProduct(
    @PrimaryKey
    val barcode: String,
    val foodId: Int
)
