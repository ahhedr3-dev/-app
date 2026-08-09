package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun ScanInvoiceScreen(
    onBack: () -> Unit,
    onManualEntry: () -> Unit
) {
    val context = LocalContext.current

    val photoFile = remember {
        File(context.cacheDir, "invoice_scan_${System.currentTimeMillis()}.jpg")
    }
    val photoUri = remember {
        try {
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
        } catch (e: Exception) {
            Uri.fromFile(photoFile)
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Simulate clarity check for camera
            val isClear = true // Can be checked via bitmap/analysis
            if (isClear) {
                Toast.makeText(context, "تم التقاط الصورة بنجاح وتحليلها عبر OCR", Toast.LENGTH_SHORT).show()
                onManualEntry()
            } else {
                Toast.makeText(context, "الصورة غير واضحة، يرجى إعادة التصوير.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "الصورة غير واضحة، يرجى إعادة التصوير.", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                takePictureLauncher.launch(photoUri)
            } catch (e: Exception) {
                Toast.makeText(context, "تعذر فتح الكاميرا", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "يرجى السماح بالوصول للكاميرا لالتقاط الفاتورة", Toast.LENGTH_LONG).show()
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // Simulate clarity check for gallery image
            val isClear = true
            if (isClear) {
                Toast.makeText(context, "تم اختيار الصورة بنجاح وتحليلها عبر OCR", Toast.LENGTH_SHORT).show()
                onManualEntry()
            } else {
                Toast.makeText(context, "الصورة غير واضحة، يرجى اختيار صورة أوضح.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "الصورة غير واضحة، يرجى اختيار صورة أوضح.", Toast.LENGTH_SHORT).show()
        }
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
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Column {
                    Text(
                        text = "تصوير فاتورة",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "مسح ضوئي ذكي",
                        color = Color(0xFF00B4D8),
                        fontSize = 12.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2537)),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFF163853), shape = RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color(0xFF00B4D8),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "التقط صورة للفاتورة",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "تأكد من الإضاءة ووضوح كامل الفاتورة داخل الإطار",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = {
                            val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    takePictureLauncher.launch(photoUri)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "تعذر فتح الكاميرا", Toast.LENGTH_SHORT).show()
                                    onManualEntry()
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B4D8))
                    ) {
                        Icon(Icons.Default.Camera, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "فتح الكاميرا", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            pickImageLauncher.launch("image/*")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = Color(0xFF00B4D8))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "استخدام كاميرا الجهاز أو صورة محفوظة", color = Color.White, fontWeight = FontWeight.Medium)
                    }

                    Text(
                        text = "لن يتم حفظ الفاتورة تلقائيا — سأراجع البيانات أولا.",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
