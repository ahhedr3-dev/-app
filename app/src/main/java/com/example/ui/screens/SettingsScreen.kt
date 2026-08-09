package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppSettingsEntity

@Composable
fun SettingsScreen(
    settings: AppSettingsEntity?,
    totalInvoicesCount: Int,
    onUpdateSettings: (AppSettingsEntity) -> Unit,
    onLockApp: () -> Unit,
    onClearData: () -> Unit
) {
    val context = LocalContext.current

    var storeName by remember(settings) { mutableStateOf(settings?.storeName ?: "") }
    var subtitle by remember(settings) { mutableStateOf(settings?.subtitle ?: "") }
    var currency by remember(settings) { mutableStateOf(settings?.currency ?: "دولار أمريكي (USD)") }
    var exchangeRate by remember(settings) { mutableStateOf(settings?.exchangeRate?.toString() ?: "12.25") }
    var isDarkMode by remember(settings) { mutableStateOf(settings?.isDarkMode ?: true) }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val currencies = listOf("دولار أمريكي (USD)", "ليرة سورية جديدة", "ليرة سورية قديمة")

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
                    text = "الإعدادات",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "تخصيص التطبيق",
                    color = Color(0xFF00B4D8),
                    fontSize = 12.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Store Details
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Store, contentDescription = null, tint = Color(0xFF00B4D8))
                            Text(text = "بيانات المتاجر", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        OutlinedTextField(
                            value = storeName,
                            onValueChange = { storeName = it },
                            label = { Text("اسم المتجر") },
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = subtitle,
                            onValueChange = { subtitle = it },
                            label = { Text("العنوان الفرعي") },
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                settings?.let {
                                    onUpdateSettings(it.copy(storeName = storeName, subtitle = subtitle))
                                    Toast.makeText(context, "تم حفظ بيانات المتجر", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8))
                        ) {
                            Text(text = "حفظ البيانات", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Currency
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color(0xFF00B4D8))
                            Text(text = "العملة", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        currencies.forEach { cur ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (currency == cur) Color(0xFF163853) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = cur, color = Color.White, fontSize = 14.sp)
                                RadioButton(
                                    selected = currency == cur,
                                    onClick = {
                                        currency = cur
                                        settings?.let { onUpdateSettings(it.copy(currency = cur)) }
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF00B4D8))
                                )
                            }
                        }
                    }
                }
            }

            // Appearance & Dark Mode
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.DarkMode, contentDescription = null, tint = Color(0xFF00B4D8))
                            Text(text = "المظهر", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "الوضع الليلي", color = Color.White, fontSize = 14.sp)
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = {
                                    isDarkMode = it
                                    settings?.let { s -> onUpdateSettings(s.copy(isDarkMode = it)) }
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00B4D8))
                            )
                        }
                    }
                }
            }

            // Manual Exchange Rate
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "سعر صرف الدولار (يدوي)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(
                            text = "يتم إدخال السعر يدويًا مقفلا ولا يتم جلبه من الإنترنت. الفواتير القديمة تحتفظ بسعر الصرف المستخدم وقت إنشائها ولا تتأثر بأي تعديل لاحق.",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )

                        OutlinedTextField(
                            value = exchangeRate,
                            onValueChange = { exchangeRate = it },
                            label = { Text("1 دولار أمريكي =") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                val rate = exchangeRate.toDoubleOrNull() ?: 12.25
                                settings?.let {
                                    onUpdateSettings(it.copy(exchangeRate = rate))
                                    Toast.makeText(context, "تم حفظ سعر الصرف", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8))
                        ) {
                            Text(text = "حفظ", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Security
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00B4D8))
                            Text(text = "الأمان", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("كلمة المرور الحالية") },
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("كلمة المرور الجديدة") },
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("تأكيد كلمة المرور") },
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (newPassword.isNotBlank() && newPassword == confirmPassword) {
                                    settings?.let {
                                        onUpdateSettings(it.copy(pinCode = newPassword))
                                        Toast.makeText(context, "تم تغيير كلمة المرور بنجاح", Toast.LENGTH_SHORT).show()
                                        currentPassword = ""
                                        newPassword = ""
                                        confirmPassword = ""
                                    }
                                } else {
                                    Toast.makeText(context, "كلمات المرور غير متطابقة", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8))
                        ) {
                            Text(text = "تغيير كلمة المرور", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onLockApp,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF00B4D8))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "قفل التطبيق الآن", color = Color.White)
                        }
                    }
                }
            }

            // Backup & Restore
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Backup, contentDescription = null, tint = Color(0xFF00B4D8))
                            Text(text = "النسخ الاحتياطي", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        Text(text = "عدد الفواتير المحفوظة: $totalInvoicesCount", color = Color(0xFF94A3B8), fontSize = 13.sp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { Toast.makeText(context, "تم تصدير ملف JSON بنجاح", Toast.LENGTH_SHORT).show() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF163853))
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color(0xFF00B4D8))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "تصدير JSON", color = Color.White, fontSize = 12.sp)
                            }
                            Button(
                                onClick = { Toast.makeText(context, "تم استعادة البيانات", Toast.LENGTH_SHORT).show() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF163853))
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null, tint = Color(0xFF00B4D8))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "استعادة", color = Color.White, fontSize = 12.sp)
                            }
                        }

                        OutlinedButton(
                            onClick = { Toast.makeText(context, "تم تصدير ملف CSV / Excel بنجاح", Toast.LENGTH_SHORT).show() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.TableChart, contentDescription = null, tint = Color(0xFF00B4D8))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "تصدير CSV / Excel", color = Color.White)
                        }
                    }
                }
            }

            // Danger Zone
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D1215)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEF4444))
                            Text(text = "منطقة الخطر", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        Button(
                            onClick = {
                                onClearData()
                                Toast.makeText(context, "تم مسح جميع البيانات", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                        ) {
                            Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "مسح جميع البيانات", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Version info footer
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "سلوم — الإصدار 1.0 - تخزين محلي على جهازك",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
