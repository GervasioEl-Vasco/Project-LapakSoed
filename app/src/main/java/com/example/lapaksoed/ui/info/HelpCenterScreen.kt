package com.example.lapaksoed.ui.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpCenterScreen(onBack: () -> Unit) {
    val blueBg = Color(0xFF0924A5)
    val yellowBtn = Color(0xFFFFD600)
    var searchQuery by remember { mutableStateOf("") }

    val allFaqs = listOf(
        "Bagaimana cara mengubah nomor telepon saya?" to
            "Pergi ke Update Profil → Kontak Utama → Nomor Telepon, lalu klik 'Ubah & Verifikasi Ulang'.",
        "Mengapa transaksi saya berstatus pending?" to
            "Transaksi berstatus pending jika pembayaran belum dikonfirmasi atau sedang diverifikasi oleh sistem. Tunggu hingga 1x24 jam.",
        "Cara daftar dan verifikasi menjadi Mitra?" to
            "Buka menu Profil → Daftar Sebagai Mitra → isi formulir pendaftaran dengan data yang valid dan lengkap.",
        "Apa yang harus dilakukan jika lupa password?" to
            "Pada halaman login, klik 'Forgot Password'. Link reset akan dikirimkan ke email yang terdaftar.",
        "Bagaimana cara top-up saldo LapakSoed?" to
            "Saldo dapat diisi melalui menu Pembayaran. Pilih metode yang tersedia dan ikuti instruksi yang diberikan."
    )

    val filteredFaqs = if (searchQuery.isEmpty()) allFaqs
    else allFaqs.filter {
        it.first.contains(searchQuery, ignoreCase = true) ||
        it.second.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pusat Bantuan", fontWeight = FontWeight.Bold) },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Banner
            Box(
                modifier = Modifier.fillMaxWidth().background(blueBg).padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.HeadsetMic, null, tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("PUSAT BANTUAN MITRA", color = yellowBtn, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        Text("Ada yang bisa kami bantu, Bud", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(25.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Search, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        decorationBox = { inner ->
                            if (searchQuery.isEmpty()) Text("Cari jawaban, kendala, atau topik lain...", color = Color.Gray, fontSize = 14.sp)
                            inner()
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        Icon(Icons.Default.Close, null, tint = Color.Gray, modifier = Modifier.size(18.dp).clickable { searchQuery = "" })
                    }
                }
            }

            // Daftar & Panduan Mitra Banner
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = yellowBtn),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable {}.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Store, null, tint = blueBg, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Daftar & Panduan Mitra", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = blueBg)
                            Box(modifier = Modifier.background(blueBg, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text("BARU", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("Panduan daftar, komisi, & aktivasi cepat", fontSize = 12.sp, color = blueBg.copy(alpha = 0.7f))
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = blueBg)
                }
            }

            // Layanan Pengaduan Aktif
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Flag, null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Layanan Pengaduan Aktif", fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) { Text("1 Berjalan", color = Color(0xFFD97706), fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("#TK-8921", color = blueBg, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFFEF3C7), RoundedCornerShape(4.dp))
                                        .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) { Text("• Sedang Diproses", color = Color(0xFFD97706), fontSize = 10.sp) }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Kendala Verifikasi Pembayaran Saldo QRS Mitra", fontSize = 13.sp)
                            Text("Dilaporkan: 4 hari yang lalu", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = blueBg),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.AddCircleOutline, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Buat Laporan / Tiket Baru", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Kategori Bantuan
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Kategori Bantuan", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Topik Umum", fontSize = 12.sp, color = blueBg)
                }
                Spacer(modifier = Modifier.height(10.dp))
                val categories = listOf(
                    Triple(Icons.Outlined.ManageAccounts, "Cara Pakai & Akun", "Ubah data, login, profil"),
                    Triple(Icons.Outlined.AccountBalanceWallet, "Pembayaran & Saldo", "Top-up, refund, transfer"),
                    Triple(Icons.Outlined.ReceiptLong, "Transaksi Mitra", "Pesanan, komisi, tiket"),
                    Triple(Icons.Outlined.Security, "Keamanan & Privasi", "PIN, OTP, verifikasi 2FA")
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            row.forEach { (icon, title, subtitle) ->
                                Card(
                                    modifier = Modifier.weight(1f).clickable {},
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Box(
                                            modifier = Modifier.size(40.dp).background(yellowBtn.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                                            contentAlignment = Alignment.Center
                                        ) { Icon(icon, null, tint = Color(0xFFD97706), modifier = Modifier.size(22.dp)) }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(subtitle, fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // FAQ
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text("Pertanyaan Paling Sering Diajukan", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))
                if (filteredFaqs.isEmpty()) {
                    Text("Tidak ada hasil untuk \"$searchQuery\"", color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 8.dp))
                }
                filteredFaqs.forEach { (q, a) ->
                    HelpFaqItem(question = q, answer = a)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Panduan & Tutorial
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Panduan & Tutorial", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Artikel Terbaru", fontSize = 12.sp, color = blueBg)
                }
                Spacer(modifier = Modifier.height(10.dp))
                TutorialCard(Icons.Outlined.PlayCircleOutline, "PANDUAN AKUN", "Daftar Akun Baru", "Langkah awal penggunaan fitur akun", blueBg)
                Spacer(modifier = Modifier.height(8.dp))
                TutorialCard(Icons.Outlined.VerifiedUser, "PROTEKSI AKUN", "Tips Keamanan Akun & Saldo", "Cegah phishing dan penipuan online", blueBg)
            }

            // Hubungi Kami Langsung
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Hubungi Kami Langsung", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Respon Cepat", fontSize = 12.sp, color = Color(0xFF22C55E))
                }
                Spacer(modifier = Modifier.height(10.dp))
                ContactCard(Icons.Outlined.Chat, "Live Chat 24/7", "Terhubung dengan Customer Care", "Online", Color(0xFF22C55E), blueBg)
                Spacer(modifier = Modifier.height(8.dp))
                ContactCard(Icons.Outlined.Email, "Kirim Email Dukungan", "support@lapaksoed.com", blueBg = blueBg)
                Spacer(modifier = Modifier.height(8.dp))
                ContactCard(Icons.Outlined.Phone, "Call Center Resmi", "021-500-888 (Bebas Pulsa)", blueBg = blueBg)
            }

            // Footer
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalDivider(color = Color.LightGray)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Syarat & Ketentuan Layanan", color = blueBg, fontSize = 11.sp)
                    Text("•", fontSize = 11.sp, color = Color.Gray)
                    Text("Kebijakan Privasi", color = blueBg, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Versi Aplikasi: 4.32.0 • © Hak Cipta Dilindungi", color = Color.Gray, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HelpFaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(question, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(answer, fontSize = 13.sp, color = Color.Gray, lineHeight = 20.sp)
                }
            }
        }
    }
}

@Composable
private fun TutorialCard(icon: ImageVector, tag: String, title: String, subtitle: String, blueBg: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(blueBg.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, null, tint = blueBg, modifier = Modifier.size(26.dp)) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text(tag, fontSize = 9.sp, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}

@Composable
private fun ContactCard(icon: ImageVector, title: String, subtitle: String, badge: String? = null, badgeColor: Color = Color.Gray, blueBg: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).background(blueBg.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) { Icon(icon, null, tint = blueBg, modifier = Modifier.size(24.dp)) }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    if (badge != null) {
                        Box(
                            modifier = Modifier
                                .background(badgeColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .border(1.dp, badgeColor.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) { Text(badge, color = badgeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
                    }
                }
                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
        }
    }
}
