package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val storeName: String = "سلوم للإنارة والأدوات الكهربائية",
    val subtitle: String = "نظام إدارة الفواتير والأرشفة",
    val currency: String = "دولار أمريكي (USD)",
    val exchangeRate: Double = 12.25,
    val isDarkMode: Boolean = true,
    val pinCode: String = "0000",
    val autoLockMinutes: Int = 0
)
