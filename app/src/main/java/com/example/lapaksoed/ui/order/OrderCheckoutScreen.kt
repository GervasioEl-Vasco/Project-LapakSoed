package com.example.lapaksoed.ui.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapaksoed.R

@Composable
fun OrderCheckoutScreen(
    viewModel: OrderViewModel,
    onBack: () -> Unit,
    onOrderCreated: () -> Unit
) {
    val listing by viewModel.selectedListing.collectAsState()
    val method by viewModel.paymentMethod.collectAsState()
    val submission by viewModel.submissionState.collectAsState()
    val paymentMethods = listOf(
        "Transfer Bank (BNI)" to "BANK_TRANSFER",
        "Dana" to "DANA",
        "Gopay" to "GOPAY",
        "QRis" to "QRIS",
        "Cash on Delivery" to "CASH_ON_DELIVERY"
    )

    LaunchedEffect(submission) {
        if (submission is OrderSubmissionState.Success) onOrderCreated()
    }

    Scaffold(containerColor = Color.Transparent) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = painterResource(R.drawable.bg_home),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White, modifier = Modifier.clickable(onClick = onBack))
                    Text("Checkout", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
                }
                Spacer(Modifier.height(20.dp))

                if (listing == null) {
                    Text("Barang tidak tersedia. Silakan kembali ke katalog.", color = Color.White)
                    return@Column
                }

                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(listing!!.title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(listing!!.category, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Jumlah: 1", color = MaterialTheme.colorScheme.onSurface)
                            Text("Rp ${listing!!.price.toLong()}", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))

                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text("Metode pembayaran", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        paymentMethods.forEach { (label, _) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable { viewModel.setPaymentMethod(label) }.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = method == label, onClick = { viewModel.setPaymentMethod(label) })
                                Text(label, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                when (val state = submission) {
                    is OrderSubmissionState.Error -> Text(state.message, color = Color(0xFFFFCDD2), modifier = Modifier.padding(4.dp))
                    else -> Unit
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = viewModel::placeOrder,
                    enabled = submission !is OrderSubmissionState.Loading,
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (submission is OrderSubmissionState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.height(24.dp), color = Color(0xFF0924A5))
                    } else {
                        Text("BUAT PESANAN", color = Color(0xFF0924A5), fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    }
                }
            }
        }
    }
}
