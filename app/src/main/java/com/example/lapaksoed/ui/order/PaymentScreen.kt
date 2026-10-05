package com.example.lapaksoed.ui.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.TabletMac
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapaksoed.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: OrderViewModel,
    onPay: () -> Unit,
    onBack: () -> Unit
) {
    val deviceCategory by viewModel.deviceCategory.collectAsState()
    val complaint by viewModel.complaint.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val pickupLocation by viewModel.pickupLocation.collectAsState()

    val blueBg = Color(0xFF0924A5)
    val yellowBtn = Color(0xFFFFD600)
    val textBlue = Color(0xFF0924A5)

    var showError by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_home),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    ),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "←",
                        color = Color.Black,
                        fontSize = 24.sp,
                        modifier = Modifier.clickable { onBack() }.padding(end = 16.dp)
                    )
                    Text(
                        text = "Jasa Service",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .drawBehind {
                                val strokeWidth = 2.dp.toPx()
                                val y = size.height
                                drawLine(
                                    color = Color(0xFF0924A5), // blue underline
                                    start = androidx.compose.ui.geometry.Offset(0f, y),
                                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                                    strokeWidth = strokeWidth
                                )
                            }
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Pilih Kategori Perangkat
                Box(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)).border(1.dp, yellowBtn, RoundedCornerShape(8.dp)).padding(8.dp), contentAlignment = Alignment.Center) {
                    Text("Pilih Kategori Perangkat", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    DeviceCategoryItem("Smartphone", Icons.Default.Smartphone, deviceCategory, viewModel::setDeviceCategory)
                    DeviceCategoryItem("Laptop/PC", Icons.Default.Laptop, deviceCategory, viewModel::setDeviceCategory)
                    DeviceCategoryItem("Tablet", Icons.Default.TabletMac, deviceCategory, viewModel::setDeviceCategory)
                    DeviceCategoryItem("Komponen Lain", Icons.Default.Memory, deviceCategory, viewModel::setDeviceCategory)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Keluhan
                Box(modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)).border(1.dp, yellowBtn, RoundedCornerShape(8.dp)).padding(8.dp), contentAlignment = Alignment.Center) {
                    Text("Keluhan", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = complaint,
                    onValueChange = { viewModel.setComplaint(it) },
                    modifier = Modifier.fillMaxWidth().height(120.dp).background(Color.White, RoundedCornerShape(12.dp)),
                    placeholder = { Text("Cth: LCD Rusak, Mati Total") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Pengambilan Perangkat
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Pengambilan Perangkat", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { viewModel.setPickupLocation("Lokasi Saat Ini") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = if (pickupLocation.isNotEmpty()) Color.LightGray else Color.White),
                            border = BorderStroke(1.dp, textBlue)
                        ) {
                            Text("Gunakan Lokasi Saat Ini", color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Metode Pembayaran
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Metode Pembayaran", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, modifier = Modifier.align(Alignment.CenterHorizontally))
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        PaymentMethodItem("Transfer Bank (BNI)", Icons.Default.AccountBalance, paymentMethod, viewModel::setPaymentMethod)
                        PaymentMethodItem("Dana", Icons.Default.Money, paymentMethod, viewModel::setPaymentMethod)
                        PaymentMethodItem("Gopay", Icons.Default.Payment, paymentMethod, viewModel::setPaymentMethod)
                        PaymentMethodItem("QRis", Icons.Default.QrCode, paymentMethod, viewModel::setPaymentMethod)
                        PaymentMethodItem("Cash on Delivery", Icons.Default.Money, paymentMethod, viewModel::setPaymentMethod)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (showError) {
                    Text(text = "Harap lengkapi semua form wajib!", color = Color.White, modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally))
                }

                Button(
                    onClick = {
                        if (deviceCategory.isBlank() || complaint.isBlank() || pickupLocation.isBlank() || paymentMethod.isBlank()) {
                            showError = true
                        } else {
                            showError = false
                            onPay()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(bottom = 16.dp)
                        .border(2.dp, Color(0xFF00FF00), RoundedCornerShape(12.dp)), // Green border from Figma
                    colors = ButtonDefaults.buttonColors(containerColor = yellowBtn),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "BAYAR SEKARANG",
                        color = blueBg,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DeviceCategoryItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selectedCategory: String,
    onSelect: (String) -> Unit
) {
    val isSelected = selectedCategory == title
    val textBlue = Color(0xFF0924A5)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(if (isSelected) 2.dp else 1.dp, if (isSelected) Color(0xFFFFD600) else textBlue, RoundedCornerShape(12.dp))
            .padding(8.dp)
            .width(70.dp)
            .clickable { onSelect(title) }
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = textBlue, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, color = textBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}

@Composable
fun PaymentMethodItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selectedMethod: String,
    onSelect: (String) -> Unit
) {
    val isSelected = selectedMethod == title
    val textBlue = Color(0xFF0924A5)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, textBlue, RoundedCornerShape(8.dp))
            .clickable { onSelect(title) }
            .padding(12.dp)
    ) {
        Icon(
            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Outlined.Circle,
            contentDescription = null,
            tint = if (isSelected) textBlue else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(imageVector = icon, contentDescription = null, tint = textBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
