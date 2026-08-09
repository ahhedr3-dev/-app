package com.example.data

import kotlinx.coroutines.flow.Flow

class SaloomRepository(private val dao: SaloomDao) {
    val allInvoices: Flow<List<InvoiceEntity>> = dao.getAllInvoices()
    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()
    val settings: Flow<AppSettingsEntity?> = dao.getSettings()

    fun getItemsForInvoice(invoiceNumber: String): Flow<List<InvoiceItemEntity>> {
        return dao.getItemsForInvoice(invoiceNumber)
    }

    suspend fun insertInvoice(invoice: InvoiceEntity, items: List<InvoiceItemEntity>) {
        dao.insertInvoice(invoice)
        dao.insertItems(items)
        for (item in items) {
            dao.insertProduct(
                ProductEntity(
                    name = item.productName,
                    totalSold = item.quantity,
                    totalRevenue = item.totalPrice
                )
            )
        }
    }

    suspend fun deleteInvoice(invoiceNumber: String) {
        dao.deleteItemsForInvoice(invoiceNumber)
        dao.deleteInvoice(invoiceNumber)
    }

    suspend fun updateSettings(settings: AppSettingsEntity) {
        dao.updateSettings(settings)
    }

    suspend fun clearAllData() {
        dao.deleteAllInvoices()
        dao.deleteAllItems()
        dao.deleteAllProducts()
    }
}
