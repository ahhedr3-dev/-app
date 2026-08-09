package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val invoiceNumber: String,
    val date: String,
    val salesperson: String,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val totalAmount: Double,
    val isZamalek: Boolean = false,
    val status: String = "بالغ"
)
