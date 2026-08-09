package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val name: String,
    val totalSold: Int,
    val totalRevenue: Double
)
