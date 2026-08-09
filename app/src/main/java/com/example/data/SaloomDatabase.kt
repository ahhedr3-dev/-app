package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [InvoiceEntity::class, InvoiceItemEntity::class, ProductEntity::class, AppSettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SaloomDatabase : RoomDatabase() {
    abstract fun saloomDao(): SaloomDao

    companion object {
        @Volatile
        private var INSTANCE: SaloomDatabase? = null

        fun getDatabase(context: Context): SaloomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SaloomDatabase::class.java,
                    "saloom_database"
                )
                    .addCallback(SaloomDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class SaloomDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.saloomDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: SaloomDao) {
            dao.updateSettings(AppSettingsEntity())

            dao.insertInvoice(
                InvoiceEntity(
                    invoiceNumber = "INV-2026-0002",
                    date = "30 يوليو 2026",
                    salesperson = "talal",
                    customerName = "خالد",
                    customerPhone = "0911223344",
                    customerAddress = "دمشق - المزة",
                    totalAmount = 450.00,
                    isZamalek = false,
                    status = "بالغ"
                )
            )
            dao.insertItems(
                listOf(
                    InvoiceItemEntity(
                        invoiceNumber = "INV-2026-0002",
                        productName = "مروحة aledo",
                        quantity = 15,
                        unitPrice = 30.00,
                        totalPrice = 450.00
                    )
                )
            )

            dao.insertInvoice(
                InvoiceEntity(
                    invoiceNumber = "INV-2026-0001",
                    date = "30 يوليو 2026",
                    salesperson = "haider",
                    customerName = "محمد",
                    customerPhone = "0955667788",
                    customerAddress = "حلب - السريان",
                    totalAmount = 150.00,
                    isZamalek = false,
                    status = "بالغ"
                )
            )
            dao.insertItems(
                listOf(
                    InvoiceItemEntity(
                        invoiceNumber = "INV-2026-0001",
                        productName = "كابل سحب",
                        quantity = 50,
                        unitPrice = 1.50,
                        totalPrice = 75.00
                    ),
                    InvoiceItemEntity(
                        invoiceNumber = "INV-2026-0001",
                        productName = "مطرقة",
                        quantity = 5,
                        unitPrice = 15.00,
                        totalPrice = 75.00
                    )
                )
            )

            dao.insertProduct(ProductEntity(name = "كابل سحب", totalSold = 50, totalRevenue = 75.00))
            dao.insertProduct(ProductEntity(name = "مروحة aledo", totalSold = 15, totalRevenue = 450.00))
            dao.insertProduct(ProductEntity(name = "مطرقة", totalSold = 5, totalRevenue = 75.00))
        }
    }
}
