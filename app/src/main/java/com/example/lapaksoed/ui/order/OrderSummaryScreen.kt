package com.example.lapaksoed.ui.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapaksoed.R

@Composable
fun OrderSummaryScreen(
    viewModel: OrderViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val product by viewModel.selectedListing.collectAsState()
    val quantity by viewModel.quantity.collectAsState()

    val blueBg = Color(0xFF0924A5)
    val yellowBtn = Color(0xFFFFD600)
    val textBlue = Color(0xFF0924A5)

    if (product == null) {
        onBack()
        return
    }

    val total = product!!.price * quantity

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
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "←",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.clickable { onBack() }.padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Ringkasan Pembayaran",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(32.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Address Box matching Image 6
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = yellowBtn, thickness = 2.dp)
                Text(" Alamat ", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Divider(modifier = Modifier.weight(1f), color = yellowBtn, thickness = 2.dp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().height(100.dp).border(1.dp, textBlue, RoundedCornerShape(12.dp))
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Alamat Pengguna", color = Color.Black, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Summary Info (Figma shows 'Alamat' again due to typo, we follow it)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = yellowBtn, thickness = 2.dp)
                Text(" Alamat ", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                Divider(modifier = Modifier.weight(1f), color = yellowBtn, thickness = 2.dp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().border(1.dp, textBlue, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Harga barang", color = Color.Black)
                        Text(text = "Rp ${product!!.price.toLong()}", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Jumlah", color = Color.Black)
                        Text(text = "x$quantity", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total", color = Color.Black, fontWeight = FontWeight.Bold)
                        Text(text = "Rp ${total.toLong()}", color = textBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onNext,
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
