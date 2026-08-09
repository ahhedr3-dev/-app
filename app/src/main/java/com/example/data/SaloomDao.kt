package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SaloomDao {
    @Query("SELECT * FROM invoices ORDER BY invoiceNumber DESC")
    fun getAllInvoices(): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoice_items WHERE invoiceNumber = :invoiceNumber")
    fun getItemsForInvoice(invoiceNumber: String): Flow<List<InvoiceItemEntity>>

    @Query("SELECT * FROM products ORDER BY totalSold DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM app_settings WHERE id = 1")
    fun getSettings(): Flow<AppSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: InvoiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<InvoiceItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSettings(settings: AppSettingsEntity)

    @Query("DELETE FROM invoices WHERE invoiceNumber = :invoiceNumber")
    suspend fun deleteInvoice(invoiceNumber: String)

    @Query("DELETE FROM invoice_items WHERE invoiceNumber = :invoiceNumber")
    suspend fun deleteItemsForInvoice(invoiceNumber: String)

    @Query("DELETE FROM invoices")
    suspend fun deleteAllInvoices()

    @Query("DELETE FROM invoice_items")
    suspend fun deleteAllItems()

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
