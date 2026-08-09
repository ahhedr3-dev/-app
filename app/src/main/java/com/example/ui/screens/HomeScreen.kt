package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppSettingsEntity
import com.example.data.InvoiceEntity
import com.example.data.ProductEntity

@Composable
fun HomeScreen(
    settings: AppSettingsEntity?,
    invoices: List<InvoiceEntity>,
    products: List<ProductEntity>,
    onNavigate: (String) -> Unit,
    onOpenInvoice: (String) -> Unit
) {
    val storeName = settings?.storeName ?: "سلوم للإنارة والأدوات الكهربائية"
    val subtitle = settings?.subtitle ?: "نظام إدارة الفواتير والأرشفة"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07111E))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Greeting Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مرحبا بعودتك 💡",
                            color = Color(0xFF00B4D8),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(onClick = { onNavigate("settings") }) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF94A3B8))
                        }
                    }
                    Text(
                        text = storeName,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Stats Cards Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "عدد الفواتير",
                        value = "${invoices.size}",
                        icon = Icons.Default.Description,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "مبيعات اليوم",
                        value = "$0.00",
                        icon = Icons.Default.AttachMoney,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "إجمالي الأرباح",
                        value = "$180.00",
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "عدد العملاء",
                        value = "${invoices.distinctBy { it.customerName }.size}",
                        icon = Icons.Default.People,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Actions Grid Title
        item {
            Text(
                text = "إجراءات سريعة",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Quick Actions 8 items
        item {
            val actions = listOf(
                Triple("فاتورة جديدة", Icons.Default.AddCircle, "new_invoice"),
                Triple("بحث", Icons.Default.Search, "search"),
                Triple("تصوير", Icons.Default.CameraAlt, "scan"),
                Triple("كل الملفات", Icons.Default.Folder, "invoices"),
                Triple("زمألك", Icons.Default.Star, "invoices"),
                Triple("حوام", Icons.Default.Repeat, "invoices"),
                Triple("السجل", Icons.Default.History, "invoices"),
                Triple("التقارير", Icons.Default.BarChart, "reports")
            )

            // 4 columns grid using rows
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                actions.chunked(4).forEach { rowActions ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowActions.forEach { (label, icon, route) ->
                            QuickActionButton(
                                label = label,
                                icon = icon,
                                modifier = Modifier.weight(1f),
                                onClick = { onNavigate(route) }
                            )
                        }
                        // Fill empty space if chunk < 4
                        repeat(4 - rowActions.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Recent Invoices Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "أحدث الفواتير",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { onNavigate("invoices") }) {
                    Text(text = "عرض الكل", color = Color(0xFF00B4D8), fontSize = 13.sp)
                }
            }
        }

        // Recent Invoices List
        items(invoices.take(3)) { invoice ->
            InvoiceCard(invoice = invoice, onClick = { onOpenInvoice(invoice.invoiceNumber) })
        }

        // Top Selling Products Header
        item {
            Text(
                text = "الأكثر مبيعاً",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Top Products
        items(products.take(3).mapIndexed { index, product -> Pair(index + 1, product) }) { (rank, product) ->
            TopProductCard(rank = rank, product = product)
        }

        // Programmer Credit / Developer Dedication Card
        item {
            DeveloperDedicationCard()
        }
    }
}

@Composable
fun DeveloperDedicationCard() {
    val context = LocalContext.current
    val isReducedMotion = try {
        val durationScale = android.provider.Settings.Global.getFloat(
            context.contentResolver,
            android.provider.Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        )
        durationScale == 0f
    } catch (e: Exception) {
        false
    }

    val infiniteTransition = rememberInfiniteTransition(label = "gold_shimmer")
    val shimmerTranslate = if (!isReducedMotion) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer"
        ).value
    } else {
        0f
    }

    val goldBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFBF953F),
            Color(0xFFFCF6BA),
            Color(0xFFB38728),
            Color(0xFFFBF5B7),
            Color(0xFFAA771C)
        ),
        start = androidx.compose.ui.geometry.Offset(shimmerTranslate - 500f, 0f),
        end = androidx.compose.ui.geometry.Offset(shimmerTranslate, 500f)
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                Toast.makeText(
                    context,
                    "This is a special dedication from programmers Haider and Talal to Hajji Muhammad Salloum.",
                    Toast.LENGTH_LONG
                ).show()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF2A2415), Color(0xFF1C170D), Color(0xFF332912))
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "A Special Edition by Programmers Haider and Talal, Dedicated to Hajji Muhammad Salloum",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Haider\nDeveloper Haider\nTalal",
                    color = Color(0xFFFFD700),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, color = Color(0xFF94A3B8), fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF163853), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF00B4D8), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun QuickActionButton(label: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (label == "فاتورة جديدة") Color(0xFF00B4D8) else Color(0xFF163853),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (label == "فاتورة جديدة") Color.Black else Color(0xFF00B4D8),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 11.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun InvoiceCard(invoice: InvoiceEntity, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format("%.2f", invoice.totalAmount)}",
                    color = Color(0xFF00B4D8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "1 صنف", color = Color(0xFF94A3B8), fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun TopProductCard(rank: Int, product: ProductEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
        modifier = Modifier.fillMaxWidth()
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
                        .size(32.dp)
                        .background(Color(0xFF163853), shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$rank", color = Color(0xFF00B4D8), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Text(text = product.name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "قطعة ${product.totalSold}", color = Color(0xFF94A3B8), fontSize = 13.sp)
                Text(
                    text = "$${String.format("%.2f", product.totalRevenue)}",
                    color = Color(0xFF00B4D8),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
