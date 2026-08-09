package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InvoiceEntity
import com.example.data.InvoiceItemEntity

data class ItemDraft(
    var productName: String = "",
    var quantity: String = "1",
    var unitPrice: String = "0.00"
)

@Composable
fun NewInvoiceScreen(
    existingInvoicesCount: Int,
    onSaveInvoice: (InvoiceEntity, List<InvoiceItemEntity>) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val invoiceNumber = remember { "INV-2026-${String.format("%04d", existingInvoicesCount + 3)}" }
    var salesperson by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerAddress by remember { mutableStateOf("") }

    val items = remember { mutableStateListOf(ItemDraft()) }

    val totalAmount = items.sumOf {
        val q = it.quantity.toIntOrNull() ?: 0
        val p = it.unitPrice.toDoubleOrNull() ?: 0.0
        q * p
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07111E))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Column {
                    Text(
                        text = "فاتورة جديدة",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "إدخال يدوي",
                        color = Color(0xFF00B4D8),
                        fontSize = 12.sp
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "بيانات الفاتورة", color = Color(0xFF00B4D8), fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        OutlinedTextField(
                            value = invoiceNumber,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("رقم الفاتورة *") },
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = salesperson,
                            onValueChange = { salesperson = it },
                            label = { Text("اسم البائع المسؤول *") },
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "بيانات العميل (اختياري)", color = Color(0xFF00B4D8), fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text("اسم العميل") },
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { customerPhone = it },
                            label = { Text("رقم الهاتف (09xxxxxxxx)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = customerAddress,
                            onValueChange = { customerAddress = it },
                            label = { Text("العنوان (المدينة، الحي...)") },
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "المنتجات", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    TextButton(onClick = { items.add(ItemDraft()) }) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF00B4D8))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "إضافة منتج", color = Color(0xFF00B4D8))
                    }
                }
            }

            itemsIndexed(items) { index, item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "المنتج ${index + 1}", color = Color(0xFF94A3B8), fontSize = 13.sp)
                            if (items.size > 1) {
                                IconButton(onClick = { items.removeAt(index) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        OutlinedTextField(
                            value = item.productName,
                            onValueChange = { items[index] = item.copy(productName = it) },
                            label = { Text("اسم المنتج") },
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = item.quantity,
                                onValueChange = { items[index] = item.copy(quantity = it) },
                                label = { Text("الكمية") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(10.dp),
                                colors = textFieldColors(),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = item.unitPrice,
                                onValueChange = { items[index] = item.copy(unitPrice = it) },
                                label = { Text("سعر الوحدة") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(10.dp),
                                colors = textFieldColors(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Total & Save Button
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "الإجمالي الكلي", color = Color(0xFF94A3B8), fontSize = 16.sp)
                    Text(text = "$${String.format("%.2f", totalAmount)}", color = Color(0xFF00B4D8), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        if (salesperson.isBlank()) {
                            Toast.makeText(context, "يرجى إدخال اسم البائع.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (items.isEmpty() || items.any { it.productName.isBlank() }) {
                            Toast.makeText(context, "يجب إضافة منتج واحد على الأقل.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val finalCustomer = if (customerName.isBlank()) "عميل نقدي" else customerName
                        val invoice = InvoiceEntity(
                            invoiceNumber = invoiceNumber,
                            date = "30 يوليو 2026",
                            salesperson = salesperson,
                            customerName = finalCustomer,
                            customerPhone = customerPhone.ifBlank { "-" },
                            customerAddress = customerAddress.ifBlank { "-" },
                            totalAmount = totalAmount,
                            status = "بالغ"
                        )
                        val invoiceItems = items.map {
                            val q = it.quantity.toIntOrNull() ?: 1
                            val p = it.unitPrice.toDoubleOrNull() ?: 0.0
                            InvoiceItemEntity(
                                invoiceNumber = invoiceNumber,
                                productName = it.productName,
                                quantity = q,
                                unitPrice = p,
                                totalPrice = q * p
                            )
                        }
                        onSaveInvoice(invoice, invoiceItems)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8))
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "حفظ الفاتورة", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF00B4D8),
    unfocusedBorderColor = Color(0xFF1E3E62),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = Color(0xFF07111E),
    unfocusedContainerColor = Color(0xFF07111E)
)
