package com.example.lapaksoed.ui.profile

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapaksoed.R

@Composable
fun PartnerRegistrationScreen(viewModel: PartnerRegistrationViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()
    var businessName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(containerColor = Color.Transparent) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            Image(
                painter = painterResource(R.drawable.bg_home),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White, modifier = Modifier.clickable(onClick = onBack).padding(8.dp))
                    Text("Daftar Sebagai Mitra", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))
                when (val current = state) {
                    PartnerRegistrationState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color(0xFFFFD600)) }
                    PartnerRegistrationState.Ready -> PartnerForm(
                        businessName = businessName,
                        onBusinessNameChange = { businessName = it },
                        phone = phone,
                        onPhoneChange = { phone = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        isSubmitting = false,
                        onSubmit = { viewModel.submit(businessName, phone, description) }
                    )
                    PartnerRegistrationState.Submitting -> PartnerForm(
                        businessName = businessName,
                        onBusinessNameChange = { businessName = it },
                        phone = phone,
                        onPhoneChange = { phone = it },
                        description = description,
                        onDescriptionChange = { description = it },
                        isSubmitting = true,
                        onSubmit = { viewModel.submit(businessName, phone, description) }
                    )
                    is PartnerRegistrationState.Existing -> ApplicationCard(
                        current.application.businessName,
                        current.application.status,
                        current.application.description
                    )
                    is PartnerRegistrationState.Submitted -> ApplicationCard(
                        current.application.businessName,
                        current.application.status,
                        current.application.description
                    )
                    is PartnerRegistrationState.Error -> Column {
                        PartnerForm(
                            businessName = businessName,
                            onBusinessNameChange = { businessName = it },
                            phone = phone,
                            onPhoneChange = { phone = it },
                            description = description,
                            onDescriptionChange = { description = it },
                            isSubmitting = false,
                            onSubmit = { viewModel.submit(businessName, phone, description) }
                        )
                        Text(current.message, color = Color(0xFFB00020), modifier = Modifier.padding(top = 10.dp))
                        Button(onClick = viewModel::loadMyApplication, modifier = Modifier.padding(top = 8.dp)) { Text("Periksa status") }
                    }
                }
            }
        }
    }
}

@Composable
private fun PartnerForm(
    businessName: String,
    onBusinessNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isSubmitting: Boolean,
    onSubmit: () -> Unit
) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Informasi usaha", color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Pengajuan akan berstatus menunggu sampai ditinjau pengelola LapakSoed.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(
                value = businessName,
                onValueChange = onBusinessNameChange,
                label = { Text("Nama usaha / lapak") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = { Text("Nomor WhatsApp") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Produk atau layanan yang ditawarkan") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = onSubmit,
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (isSubmitting) CircularProgressIndicator(modifier = Modifier.height(22.dp), color = Color(0xFF0924A5))
                else Text("KIRIM PENGAJUAN", color = Color(0xFF0924A5), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ApplicationCard(name: String, status: String, description: String) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Pengajuan mitra Anda", color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text("Nama lapak: $name", color = MaterialTheme.colorScheme.onSurface)
            Text("Status: ${statusLabel(status)}", color = Color(0xFF0924A5), fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun statusLabel(status: String): String = when (status) {
    "PENDING" -> "Menunggu ditinjau"
    "APPROVED" -> "Disetujui"
    "REJECTED" -> "Belum disetujui"
    else -> status
}
