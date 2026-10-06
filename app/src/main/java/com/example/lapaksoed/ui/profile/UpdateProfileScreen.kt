package com.example.lapaksoed.ui.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE) }

    val blueBg = Color(0xFF0924A5)
    val yellowBtn = Color(0xFFFFD600)

    var fullName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Laki-laki") }
    var address by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var instagram by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var triggerSave by remember { mutableStateOf(false) }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            fullName = prefs.getString("fullName", it.fullName) ?: it.fullName
        }
        nickname = prefs.getString("nickname", "") ?: ""
        username = prefs.getString("username", "") ?: ""
        bio = prefs.getString("bio", "") ?: ""
        phone = prefs.getString("phone", "") ?: ""
        birthDate = prefs.getString("birthDate", "") ?: ""
        gender = prefs.getString("gender", "Laki-laki") ?: "Laki-laki"
        address = prefs.getString("address", "") ?: ""
        website = prefs.getString("website", "") ?: ""
        instagram = prefs.getString("instagram", "") ?: ""
        linkedin = prefs.getString("linkedin", "") ?: ""
    }

    LaunchedEffect(triggerSave) {
        if (triggerSave) {
            snackbarHostState.showSnackbar("Profil berhasil disimpan.")
            triggerSave = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Opsi")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Avatar Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .background(Color(0xFF6B3A36), CircleShape)
                                .border(3.dp, yellowBtn, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (userProfile?.fullName?.firstOrNull() ?: "?").toString().uppercase(),
                                color = Color.White,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.BottomEnd)
                                .background(yellowBtn, CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(userProfile?.fullName ?: "Memuat...", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(userProfile?.email ?: "", color = Color.Gray, fontSize = 12.sp)
                }
            }

            // Upload Foto Button
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = blueBg),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CloudUpload, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Unggah Foto")
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFFFEAEA), RoundedCornerShape(10.dp))
                        .border(1.dp, Color.Red.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                ) {
                    Icon(Icons.Default.Delete, null, tint = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Informasi Dasar
            ProfileFormSection(title = "Informasi Dasar") {
                ProfileFormField("Nama Lengkap", fullName, Icons.Default.Person) { fullName = it }
                ProfileFormField("Nama Panggilan", nickname, Icons.Default.Badge) { nickname = it }
                ProfileFormField("Username", username, Icons.Default.AlternateEmail, prefix = "@") { username = it }
                ProfileFormField("Deskripsi/Bio", bio, Icons.Default.EditNote, singleLine = false) { bio = it }
            }

            // Kontak Utama
            ProfileFormSection(title = "Kontak Utama") {
                // Email (read-only)
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("Alamat Email", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = userProfile?.email ?: "",
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Email, null, modifier = Modifier.size(20.dp)) },
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .background(blueBg.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) { Text("Terverifikasi", color = blueBg, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color.LightGray.copy(alpha = 0.5f),
                            disabledTextColor = Color.DarkGray,
                            disabledLeadingIconColor = Color.Gray
                        )
                    )
                }
                ProfileFormField(
                    "Nomor Telepon", phone, Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                ) { phone = it }
                TextButton(
                    onClick = {},
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Ubah & Verifikasi Ulang →", color = blueBg, fontSize = 12.sp)
                }
            }

            // Data Personal Tambahan
            ProfileFormSection(title = "Data Personal Tambahan") {
                ProfileFormField("Tanggal Lahir", birthDate, Icons.Default.CalendarToday, placeholder = "14 Agustus 1998") { birthDate = it }
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                    Text("Jenis Kelamin", fontSize = 12.sp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "Laki-laki",
                            onClick = { gender = "Laki-laki" },
                            colors = RadioButtonDefaults.colors(selectedColor = blueBg)
                        )
                        Text("Laki-laki", modifier = Modifier.clickable { gender = "Laki-laki" })
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = gender == "Perempuan",
                            onClick = { gender = "Perempuan" },
                            colors = RadioButtonDefaults.colors(selectedColor = blueBg)
                        )
                        Text("Perempuan", modifier = Modifier.clickable { gender = "Perempuan" })
                    }
                }
                ProfileFormField(
                    "Alamat Fisik / Domisili", address, Icons.Default.LocationOn,
                    placeholder = "Jl. Merdeka No. 45, Purwokerto, Jawa Tengah"
                ) { address = it }
            }

            // Tautan & Media Sosial
            ProfileFormSection(title = "Tautan & Media Sosial") {
                ProfileFormField("Website / Portofolio", website, Icons.Default.Language, placeholder = "https://budiawan.me") { website = it }
                ProfileFormField("Instagram", instagram, Icons.Default.Link, placeholder = "instagram.com/budiawan") { instagram = it }
                ProfileFormField("LinkedIn", linkedin, Icons.Default.Link, placeholder = "linkedin.com/in/budiawan") { linkedin = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    prefs.edit()
                        .putString("fullName", fullName)
                        .putString("nickname", nickname)
                        .putString("username", username)
                        .putString("bio", bio)
                        .putString("phone", phone)
                        .putString("birthDate", birthDate)
                        .putString("gender", gender)
                        .putString("address", address)
                        .putString("website", website)
                        .putString("instagram", instagram)
                        .putString("linkedin", linkedin)
                        .apply()
                    triggerSave = true
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = yellowBtn),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, null, tint = blueBg, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan Perubahan", color = blueBg, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileFormSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    val blueBg = Color(0xFF0924A5)
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Box(modifier = Modifier.size(width = 4.dp, height = 18.dp).background(blueBg, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            content()
        }
    }
}

@Composable
private fun ProfileFormField(
    label: String,
    value: String,
    icon: ImageVector,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    prefix: String? = null,
    onValueChange: (String) -> Unit
) {
    val blueBg = Color(0xFF0924A5)
    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            placeholder = { Text(placeholder.ifEmpty { label }, fontSize = 14.sp, color = Color.LightGray) },
            leadingIcon = { Icon(icon, null, modifier = Modifier.size(20.dp)) },
            prefix = if (prefix != null) ({ Text(prefix, color = Color.Gray) }) else null,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = blueBg,
                unfocusedBorderColor = Color.LightGray
            )
        )
    }
}
