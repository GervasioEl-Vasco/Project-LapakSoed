package com.example.lapaksoed.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lapaksoed.data.remote.PromotionResponse

@Composable
fun PromotionDetailScreen(
    promotionId: String,
    viewModel: PromotionViewModel,
    onBack: () -> Unit,
    onBrowseCatalog: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(promotionId) { viewModel.load(promotionId) }

    Scaffold(containerColor = Color.Transparent) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = androidx.compose.ui.res.painterResource(com.example.lapaksoed.R.drawable.bg_home),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White,
                    modifier = Modifier.clickable(onClick = onBack).padding(8.dp)
                )
                when (val current = state) {
                    PromotionDetailState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFFFD600))
                    }
                    PromotionDetailState.NotFound -> PromoMessage("Promosi sudah tidak tersedia.", onBack = onBack)
                    is PromotionDetailState.Error -> PromoMessage(current.message, onRetry = { viewModel.load(promotionId) })
                    is PromotionDetailState.Success -> PromotionContent(current.promotion, onBrowseCatalog)
                }
            }
        }
    }
}

@Composable
private fun PromotionContent(promotion: PromotionResponse, onBrowseCatalog: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            AsyncImage(
                model = promotion.imageUrl,
                contentDescription = promotion.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(280.dp)
            )
            Column(Modifier.padding(20.dp)) {
                Text(promotion.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Lihat barang-barang pilihan mahasiswa di LapakSoed.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = onBrowseCatalog,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("JELAJAHI KATALOG", color = Color(0xFF0924A5), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PromoMessage(
    message: String,
    onRetry: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(message, color = Color.White)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry ?: onBack ?: {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600))) {
            Text(if (onRetry != null) "Coba lagi" else "Kembali", color = Color(0xFF0924A5))
        }
    }
}
