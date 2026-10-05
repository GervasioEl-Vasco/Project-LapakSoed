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
fun SelectProductScreen(
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
                    text = "Pilih Barang",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(32.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Product Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().border(1.dp, textBlue, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product!!.title,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = product!!.category,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Rp ${product!!.price.toLong()}",
                            color = textBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Quantity selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .border(1.dp, textBlue, RoundedCornerShape(16.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-",
                            modifier = Modifier.clickable { viewModel.updateQuantity(quantity - 1) }.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = textBlue,
                            fontSize = 20.sp
                        )
                        Text(
                            text = quantity.toString(),
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "+",
                            modifier = Modifier.clickable { viewModel.updateQuantity(quantity + 1) }.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = textBlue,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = yellowBtn),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "LANJUT",
                    color = blueBg,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
