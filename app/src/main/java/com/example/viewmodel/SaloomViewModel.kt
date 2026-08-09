package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppSettingsEntity
import com.example.data.InvoiceEntity
import com.example.data.InvoiceItemEntity
import com.example.data.ProductEntity
import com.example.data.SaloomDatabase
import com.example.data.SaloomRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SaloomViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SaloomRepository

    val invoices: StateFlow<List<InvoiceEntity>>
    val products: StateFlow<List<ProductEntity>>
    val settings: StateFlow<AppSettingsEntity?>

    init {
        val dao = SaloomDatabase.getDatabase(application).saloomDao()
        repository = SaloomRepository(dao)

        invoices = repository.allInvoices.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        products = repository.allProducts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        settings = repository.settings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettingsEntity()
        )
    }

    fun getItemsForInvoice(invoiceNumber: String) = repository.getItemsForInvoice(invoiceNumber)

    fun addInvoice(invoice: InvoiceEntity, items: List<InvoiceItemEntity>) {
        viewModelScope.launch {
            repository.insertInvoice(invoice, items)
        }
    }

    fun deleteInvoice(invoiceNumber: String) {
        viewModelScope.launch {
            repository.deleteInvoice(invoiceNumber)
        }
    }

    fun updateSettings(settings: AppSettingsEntity) {
        viewModelScope.launch {
            repository.updateSettings(settings)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}
