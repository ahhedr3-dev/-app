package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InvoiceEntity

@Composable
fun InvoicesScreen(
    invoices: List<InvoiceEntity>,
    onNewInvoice: () -> Unit,
    onOpenInvoice: (String) -> Unit,
    onDeleteInvoice: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("الكل") }
    var selectedSort by remember { mutableStateOf("الأحدث") }

    val filters = listOf("الملفات", "الكل", "زمألك", "غير زمألك")
    val sorts = listOf("الأحدث", "الأقدم", "الأعلى سعرا", "الأقل سعرا")

    val filteredInvoices = invoices.filter { invoice ->
        val matchesSearch = invoice.customerName.contains(searchQuery, ignoreCase = true) ||
                invoice.invoiceNumber.contains(searchQuery, ignoreCase = true) ||
                invoice.salesperson.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "زمألك" -> invoice.isZamalek
            "غير زمألك" -> !invoice.isZamalek
            else -> true
        }

        matchesSearch && matchesFilter
    }.sortedWith(
        when (selectedSort) {
            "الأقدم" -> compareBy { it.invoiceNumber }
            "الأعلى سعرا" -> compareByDescending { it.totalAmount }
            "الأقل سعرا" -> compareBy { it.totalAmount }
            else -> compareByDescending { it.invoiceNumber } // الأحدث
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07111E))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header & New Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "الفواتير",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${invoices.size} فاتورة محفوظة",
                    color = Color(0xFF94A3B8),
                    fontSize = 13.sp
                )
            }
            Button(
                onClick = onNewInvoice,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "جديدة", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("ابحث برقم الفاتورة، اسم العميل، منتج...", color = Color(0xFF64748B)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF00B4D8)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00B4D8),
                unfocusedBorderColor = Color(0xFF1E3E62),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF0F2537),
                unfocusedContainerColor = Color(0xFF0F2537)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF00B4D8),
                        selectedLabelColor = Color.Black,
                        containerColor = Color(0xFF0F2537),
                        labelColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Sort Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(sorts) { sort ->
                FilterChip(
                    selected = selectedSort == sort,
                    onClick = { selectedSort = sort },
                    label = { Text(sort) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF1E3E62),
                        selectedLabelColor = Color(0xFF00B4D8),
                        containerColor = Color(0xFF0F2537),
                        labelColor = Color(0xFF94A3B8)
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Invoices List
        if (filteredInvoices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "لا توجد فواتير مطابقة", color = Color(0xFF94A3B8), fontSize = 15.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredInvoices) { invoice ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenInvoice(invoice.invoiceNumber) }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFF163853), shape = RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF00B4D8))
                                }
                                Column {
                                    Text(text = invoice.customerName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "${invoice.invoiceNumber} - ${invoice.date}", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = "بائع: ${invoice.salesperson}", color = Color(0xFF00B4D8), fontSize = 11.sp)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "$${String.format("%.2f", invoice.totalAmount)}",
                                        color = Color(0xFF00B4D8),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = invoice.status, color = Color(0xFF4ADE80), fontSize = 11.sp)
                                }
                                IconButton(onClick = { onDeleteInvoice(invoice.invoiceNumber) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
