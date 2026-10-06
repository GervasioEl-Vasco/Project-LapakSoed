package com.example.lapaksoed.ui.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
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
import com.example.lapaksoed.ui.theme.LapakPalette

private data class ServiceChoice(val value: String, val label: String)

@Composable
fun ServiceRequestScreen(
    viewModel: ServiceViewModel,
    onBack: () -> Unit,
    onSubmitted: () -> Unit,
) {
    val deviceCategory by viewModel.deviceCategory.collectAsState()
    val complaint by viewModel.complaint.collectAsState()
    val pickupLocation by viewModel.pickupLocation.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val submission by viewModel.submission.collectAsState()
    val categories = listOf(
        ServiceChoice("SMARTPHONE", "Smartphone"),
        ServiceChoice("LAPTOP_PC", "Laptop/PC"),
        ServiceChoice("TABLET", "Tablet"),
        ServiceChoice("OTHER_COMPONENT", "Komponen lain"),
    )
    val payments = listOf(
        ServiceChoice("BANK_TRANSFER", "Transfer Bank (BNI)"),
        ServiceChoice("DANA", "DANA"),
        ServiceChoice("GOPAY", "GoPay"),
        ServiceChoice("QRIS", "QRIS"),
        ServiceChoice("CASH_ON_DELIVERY", "Cash on Delivery"),
    )

    LaunchedEffect(submission) {
        if (submission is ServiceSubmissionState.Success) onSubmitted()
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_home),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .border(3.dp, LapakPalette.Yellow, RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier.clickable(onClick = onBack),
                )
                Spacer(Modifier.width(16.dp))
                Text("Jasa Service", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ServiceSection("Kategori perangkat") {
                    ChoiceGrid(categories, deviceCategory, viewModel::setDeviceCategory)
                }
                ServiceSection("Keluhan") {
                    OutlinedTextField(
                        value = complaint,
                        onValueChange = viewModel::setComplaint,
                        modifier = Modifier.fillMaxWidth().height(124.dp),
                        placeholder = { Text("Contoh: layar retak, perangkat mati total") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = LapakPalette.Blue,
                        ),
                    )
                }
                ServiceSection("Lokasi pengambilan") {
                    OutlinedTextField(
                        value = pickupLocation,
                        onValueChange = viewModel::setPickupLocation,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("Masukkan lokasi atau alamat") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = LapakPalette.Blue,
                        ),
                    )
                }
                ServiceSection("Metode pembayaran") {
                    payments.forEach { choice ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.setPaymentMethod(choice.value) },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = paymentMethod == choice.value,
                                onClick = { viewModel.setPaymentMethod(choice.value) },
                            )
                            Text(choice.label, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                if (submission is ServiceSubmissionState.Error) {
                    Text((submission as ServiceSubmissionState.Error).message, color = LapakPalette.Error)
                }
                Button(
                    onClick = viewModel::submit,
                    enabled = submission !is ServiceSubmissionState.Loading,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LapakPalette.Yellow, contentColor = LapakPalette.Blue),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    if (submission is ServiceSubmissionState.Loading) {
                        CircularProgressIndicator(color = LapakPalette.Blue)
                    } else {
                        Text("KIRIM PERMINTAAN", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(title, color = LapakPalette.Blue, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
private fun ChoiceGrid(
    choices: List<ServiceChoice>,
    selected: String,
    onSelected: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        choices.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { choice ->
                    val isSelected = selected == choice.value
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (isSelected) LapakPalette.Yellow else Color.Transparent, RoundedCornerShape(12.dp))
                            .border(1.dp, if (isSelected) LapakPalette.Yellow else LapakPalette.Blue, RoundedCornerShape(12.dp))
                            .clickable { onSelected(choice.value) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(choice.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = LapakPalette.Blue)
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}
