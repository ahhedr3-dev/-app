package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InvoiceEntity
import com.example.data.ProductEntity

@Composable
fun ReportsScreen(
    invoices: List<InvoiceEntity>,
    products: List<ProductEntity>
) {
    var selectedPeriod by remember { mutableStateOf("الأسبوع") }
    val periods = listOf("اليوم", "الأمس", "الأسبوع", "الشهر", "السنة")

    val totalInvoices = invoices.size
    val totalRevenue = invoices.sumOf { it.totalAmount }
    val totalProfit = totalRevenue * 0.3 // estimated profit margin

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
                    text = "التقارير",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "تحليلات المبيعات والأرباح",
                    color = Color(0xFF00B4D8),
                    fontSize = 12.sp
                )
            }
        }

        // Period Filter Tabs
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(periods) { period ->
                FilterChip(
                    selected = selectedPeriod == period,
                    onClick = { selectedPeriod = period },
                    label = { Text(period) },
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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Metrics Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ReportStatCard(
                        title = "عدد الفواتير",
                        value = "$totalInvoices",
                        modifier = Modifier.weight(1f)
                    )
                    ReportStatCard(
                        title = "إجمالي الإيرادات",
                        value = "$${String.format("%.2f", totalRevenue)}",
                        modifier = Modifier.weight(1f)
                    )
                    ReportStatCard(
                        title = "الأرباح",
                        value = "$${String.format("%.2f", totalProfit)}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Sales Trend Chart Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "اتجاه المبيعات", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(Color(0xFF07111E), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val width = size.width
                                val height = size.height
                                val points = listOf(
                                    Offset(width * 0.1f, height * 0.8f),
                                    Offset(width * 0.3f, height * 0.6f),
                                    Offset(width * 0.5f, height * 0.4f),
                                    Offset(width * 0.7f, height * 0.3f),
                                    Offset(width * 0.9f, height * 0.25f)
                                )

                                // Draw grid lines
                                for (i in 1..3) {
                                    drawLine(
                                        color = Color(0xFF1E3E62),
                                        start = Offset(0f, height * i / 4f),
                                        end = Offset(width, height * i / 4f),
                                        strokeWidth = 1f
                                    )
                                }

                                // Draw line connecting points
                                for (i in 0 until points.size - 1) {
                                    drawLine(
                                        color = Color(0xFF00B4D8),
                                        start = points[i],
                                        end = points[i + 1],
                                        strokeWidth = 3f
                                    )
                                }

                                // Draw points
                                points.forEach { pt ->
                                    drawCircle(color = Color(0xFF00B4D8), radius = 5f, center = pt)
                                    drawCircle(color = Color.White, radius = 2f, center = pt)
                                }
                            }
                        }
                    }
                }
            }

            // Best 5 Products Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "أفضل 5 منتجات", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        products.take(5).forEach { product ->
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = product.name, color = Color(0xFF94A3B8), fontSize = 13.sp)
                                    Text(text = "قطعة ${product.totalSold}", color = Color(0xFF00B4D8), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                LinearProgressIndicator(
                                    progress = { (product.totalSold / 60f).coerceIn(0.1f, 1f) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp),
                                    color = Color(0xFF00B4D8),
                                    trackColor = Color(0xFF163853),
                                )
                            }
                        }
                    }
                }
            }

            // Customer Sales Share
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "حصة العملاء من المبيعات", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        invoices.groupBy { it.customerName }.forEach { (customer, invs) ->
                            val amount = invs.sumOf { it.totalAmount }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(Color(0xFF00B4D8), RoundedCornerShape(50))
                                    )
                                    Text(text = customer, color = Color.White, fontSize = 14.sp)
                                }
                                Text(text = "$${String.format("%.2f", amount)}", color = Color(0xFF00B4D8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportStatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, color = Color(0xFF94A3B8), fontSize = 11.sp, maxLines = 1)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}
