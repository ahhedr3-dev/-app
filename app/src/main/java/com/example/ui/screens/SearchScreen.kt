package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun SearchScreen(
    invoices: List<InvoiceEntity>,
    onOpenInvoice: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val quickQueries = listOf(
        "فواتير أحمد اليوم",
        "أعلى فاتورة هذا الشهر",
        "كل فواتير اللمبات",
        "فواتير زمالك الأسبوع الماضي"
    )

    val results = invoices.filter {
        query.isBlank() ||
                it.customerName.contains(query, ignoreCase = true) ||
                it.invoiceNumber.contains(query, ignoreCase = true) ||
                it.salesperson.contains(query, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07111E))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "البحث",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ابحث في كامل الأرشيف",
                    color = Color(0xFF00B4D8),
                    fontSize = 12.sp
                )
            }
        }

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("مثال: فاتورة أحمد اليوم، أعلى فاتورة هذا الشهر...", color = Color(0xFF64748B)) },
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

        // Smart Query suggestion chips
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "اكتب أي كلمة أو جملة للبحث الذكي في الأرشيف:",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp
            )
            quickQueries.chunked(2).forEach { rowQueries ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowQueries.forEach { qq ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { query = qq }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = qq,
                                    color = Color(0xFF00B4D8),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    if (rowQueries.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "نتائج البحث (${results.size})",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(results) { invoice ->
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
                            }
                        }
                        Text(
                            text = "$${String.format("%.2f", invoice.totalAmount)}",
                            color = Color(0xFF00B4D8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
