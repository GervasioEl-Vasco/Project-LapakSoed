package com.example.lapaksoed.ui.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsPrivacyScreen(onBack: () -> Unit) {
    val blueBg = Color(0xFF0924A5)
    val yellowBtn = Color(0xFFFFD600)
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Ketentuan", "Privasi", "Hak & Data")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Ketentuan Dan Privasi", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
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
        ) {
            // Status badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1A1A2E), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("RESMI", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFF22C55E).copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .border(1.dp, Color(0xFF22C55E), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("AKTIF 4.3", color = Color(0xFF22C55E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Text("Rev. 4.3", color = Color.Gray, fontSize = 12.sp)
            }

            // Title
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    "Ketentuan Layanan & Kebijakan Privasi",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Terakhir diperbarui: 15 Januari 2025. Harap baca dengan saksama komitmen kami untuk keamanan Anda.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = blueBg,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        Box(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab])
                                .height(3.dp)
                                .background(yellowBtn)
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) blueBg else Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                when (selectedTab) {
                    0 -> TermsTabContent(blueBg)
                    1 -> PrivacyTabContent()
                    2 -> RightsTabContent()
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = blueBg.copy(alpha = 0.06f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Butuh Bantuan Hukum?", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Pertanyaan terkait kepatuhan dan perlindungan privasi data, silakan hubungi Tim Data Protection Officer kami.",
                            fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(blueBg, RoundedCornerShape(8.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Icon(Icons.Default.Email, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("privacy@lapaksoed.com", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun TermsTabContent(blueBg: Color) {
    val articles = listOf(
        Triple("PASAL 01", "Hak & Kewajiban Pengguna",
            "Setiap pengguna aplikasi memiliki hak untuk mengakses fitur, transaksi, mendapatkan perlindungan data, serta mendapatkan layanan dukungan.\n\n" +
            "✅ Wajib memberikan identitas yang sah, akurat, dan dapat diverifikasi.\n" +
            "✅ Menjaga kerahasiaan kredensial login (PIN, OTP, dan Sandi Akun)."),
        Triple("PASAL 02", "Batasan Usia & Kelayakan",
            "Pengguna wajib berusia minimal 17 tahun atau merupakan mahasiswa aktif Universitas Jenderal Soedirman yang terdaftar secara resmi."),
        Triple("PASAL 03", "Larangan Keras",
            "🚫 Manipulasi data/transaksi & penipuan dana\n🚫 Otomatisasi tak berbasis, scraping & botting\n🚫 Spamming dan tindakan pelecehan digital"),
        Triple("PASAL 04", "Sanksi Pelanggaran",
            "Pelanggaran terhadap ketentuan ini akan dikenakan sanksi bertahap: peringatan tertulis, pembatasan fitur, penangguhan akun, hingga pemblokiran akun secara permanen tanpa kompensasi."),
        Triple("LEGALITAS", "Hak Kekayaan Intelektual",
            "Semua merek dagang, logo, rancangan antarmuka, dan kode sumber adalah milik hak cipta dari pengelola platform. Pengguna dilarang mendistribusikan atau memodifikasi aplikasi ini untuk tujuan komersial tanpa izin tertulis resmi.")
    )
    articles.forEach { (tag, title, content) ->
        AccordionCard(tag, title, content, if (tag == "LEGALITAS") Color(0xFF7C3AED) else blueBg)
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun PrivacyTabContent() {
    val items = listOf(
        Triple("§ 1", "Data yang Kami Kumpulkan",
            "Kami mengumpulkan data yang Anda berikan saat registrasi (nama, email, NIM), data transaksi, serta data teknis perangkat untuk meningkatkan layanan."),
        Triple("§ 2", "Penggunaan Data",
            "Data Anda digunakan untuk:\n• Memproses transaksi dan pesanan\n• Meningkatkan keamanan akun\n• Mengirimkan notifikasi relevan\n• Analisis penggunaan layanan (anonim)"),
        Triple("§ 3", "Penyimpanan & Keamanan",
            "Data disimpan di server yang berlokasi di Indonesia dengan enkripsi standar industri. Kami menerapkan protokol keamanan berlapis untuk melindungi data Anda."),
        Triple("§ 4", "Berbagi Data",
            "Kami tidak menjual data Anda kepada pihak ketiga. Data hanya dibagikan kepada mitra layanan yang diperlukan untuk operasional platform, dengan perjanjian kerahasiaan yang ketat.")
    )
    items.forEach { (tag, title, content) ->
        AccordionCard(tag, title, content, Color(0xFF059669))
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun RightsTabContent() {
    val items = listOf(
        Triple("HAK 1", "Akses Data Anda",
            "Anda berhak meminta salinan data pribadi yang kami simpan. Permintaan dapat diajukan melalui menu 'Unduh Salinan Data' di halaman profil."),
        Triple("HAK 2", "Koreksi Data",
            "Anda dapat memperbarui informasi profil Anda kapan saja melalui menu 'Update Profil'. Untuk koreksi data sensitif, hubungi tim dukungan kami."),
        Triple("HAK 3", "Penghapusan Akun",
            "Anda berhak meminta penghapusan akun dan seluruh data terkait. Proses ini bersifat permanen dan tidak dapat dibatalkan setelah dikonfirmasi."),
        Triple("HAK 4", "Portabilitas Data",
            "Anda dapat mengekspor data transaksi dan profil Anda dalam format PDF atau CSV melalui menu pengaturan akun.")
    )
    items.forEach { (tag, title, content) ->
        AccordionCard(tag, title, content, Color(0xFFD97706))
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun AccordionCard(tag: String, title: String, content: String, tagColor: Color) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.background(tagColor, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 3.dp)
                ) { Text(tag, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                Spacer(modifier = Modifier.width(10.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.LightGray)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(content, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f), lineHeight = 20.sp)
                }
            }
        }
    }
}
