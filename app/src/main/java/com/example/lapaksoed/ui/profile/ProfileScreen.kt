package com.example.lapaksoed.ui.profile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapaksoed.R
import com.example.lapaksoed.ui.home.BottomNavigationBar

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    isDarkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToPartnerRegistration: () -> Unit,
    onNavigateToUpdateProfile: () -> Unit,
    onNavigateToTermsPrivacy: () -> Unit,
    onNavigateToHelpCenter: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    
    var isNotificationEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("notif_enabled", false)) }
    var is2FaEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("2fa_enabled", false)) }
    var isBiometricEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("biometric_enabled", false)) }
    var isEmailNotifEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("email_notif_enabled", true)) }
    
    var infoDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val yellowBtn = Color(0xFFFFD600)
    val blueBg = Color(0xFF0924A5)
    val avatarColor = Color(0xFF6B3A36) 

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                activeColor = yellowBtn,
                bgColor = blueBg,
                activeRoute = "profile",
                onNavigate = { route ->
                    when (route) {
                        "home" -> onNavigateToHome()
                        "orders" -> onNavigateToOrders()
                        "chat" -> onNavigateToChat()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            // Background
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
                        .height(90.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                        )
                        .border(
                            width = 4.dp,
                            color = yellowBtn,
                            shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(60.dp).background(avatarColor, CircleShape).border(1.dp, Color.Black, CircleShape))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "IKLAN",
                            color = yellowBtn,
                            fontSize = 32.sp,
                            fontFamily = FontFamily.Cursive,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 50.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Info Card with completeness score
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(70.dp).background(Color(0xFF5E0B0B), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (userProfile?.fullName?.firstOrNull() ?: "?").toString().uppercase(),
                                    color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = userProfile?.fullName ?: "Memuat...",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = userProfile?.email ?: "", color = Color(0xFFE5A822), fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier.background(blueBg, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier.size(16.dp).background(Color.White, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) { Text("L", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Akun LapakSoed", color = Color.White, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Completeness / Security Score
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Outlined.VerifiedUser, null, tint = Color(0xFF22C55E), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Skor Keamanan Akun", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                            Text("85%", fontWeight = FontWeight.Bold, color = Color(0xFF22C55E))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.85f },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF22C55E),
                            trackColor = Color(0xFF22C55E).copy(alpha = 0.2f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menus: Navigasi Baru
                ProfileSectionTitle("Pengaturan & Profil")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        ProfileMenuItem(Icons.Outlined.Person, "Update Profil") { onNavigateToUpdateProfile() }
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        ProfileMenuItem(Icons.Outlined.HelpOutline, "Pusat Bantuan") { onNavigateToHelpCenter() }
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        ProfileMenuItem(Icons.Outlined.Description, "Ketentuan dan Privasi") { onNavigateToTermsPrivacy() }
                    }
                }

                ProfileSectionTitle("Keamanan Akun")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        ProfileMenuItem(Icons.Outlined.Lock, "Ubah Password") {
                            infoDialog = "Ubah Password" to "Fitur ubah password dalam tahap pengembangan."
                        }
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        ToggleItemRow(
                            title = "Autentikasi 2 Faktor (2FA)",
                            subtitle = "Lapisan keamanan ekstra",
                            icon = Icons.Outlined.Security,
                            isChecked = is2FaEnabled,
                            onCheckedChange = { is2FaEnabled = it; sharedPrefs.edit().putBoolean("2fa_enabled", it).apply() }
                        )
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        ToggleItemRow(
                            title = "Login Biometrik",
                            subtitle = "Gunakan sidik jari / Face ID",
                            icon = Icons.Outlined.Fingerprint,
                            isChecked = isBiometricEnabled,
                            onCheckedChange = { isBiometricEnabled = it; sharedPrefs.edit().putBoolean("biometric_enabled", it).apply() }
                        )
                    }
                }

                ProfileSectionTitle("Metode Pembayaran")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        ProfileMenuItem(Icons.Outlined.AccountBalanceWallet, "E-Wallet (GoPay, OVO, dll)") {
                            infoDialog = "E-Wallet" to "Manajemen e-wallet akan tersedia di versi berikutnya."
                        }
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        ProfileMenuItem(Icons.Outlined.CreditCard, "Kartu Debit / Kredit") {
                            infoDialog = "Kartu" to "Pembayaran dengan kartu belum diaktifkan."
                        }
                    }
                }

                ProfileSectionTitle("Preferensi & Notifikasi")
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        ToggleItemRow(
                            title = "Notifikasi Push HP",
                            subtitle = "Promo, Transaksi & Pesan",
                            icon = Icons.Outlined.NotificationsActive,
                            isChecked = isNotificationEnabled,
                            onCheckedChange = { isNotificationEnabled = it; sharedPrefs.edit().putBoolean("notif_enabled", it).apply() }
                        )
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        ToggleItemRow(
                            title = "Email Ringkasan",
                            subtitle = "Update mingguan & promosi",
                            icon = Icons.Outlined.Email,
                            isChecked = isEmailNotifEnabled,
                            onCheckedChange = { isEmailNotifEnabled = it; sharedPrefs.edit().putBoolean("email_notif_enabled", it).apply() }
                        )
                        Divider(color = Color.LightGray.copy(alpha = 0.5f))
                        ToggleItemRow(
                            title = "Mode Gelap (Dark Mode)",
                            subtitle = "Tema aplikasi",
                            icon = Icons.Outlined.DarkMode,
                            isChecked = isDarkTheme,
                            onCheckedChange = onDarkThemeChange
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Daftar Mitra Button
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(60.dp)
                        .background(Color(0xFFF29B00), RoundedCornerShape(16.dp))
                        .clickable(onClick = onNavigateToPartnerRegistration),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Storefront, null, tint = Color.White, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Daftar Sebagai Mitra", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                        ) { Text("NEW", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ChevronRight, null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(60.dp)
                        .background(Color(0xFFFFEAEA), RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Red.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { viewModel.logout(); onLogout() },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Logout, "Log Out", tint = Color.Red)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("LOG OUT DARI AKUN INI", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    
    infoDialog?.let { (title, message) ->
        AlertDialog(
            onDismissRequest = { infoDialog = null },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = { infoDialog = null }) { Text("Tutup") } }
        )
    }
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp).background(Color(0xFF0924A5).copy(alpha = 0.8f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun ToggleItemRow(
    title: String,
    subtitle: String?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            if (subtitle != null) {
                Text(subtitle, color = Color.Gray, fontSize = 11.sp)
            }
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF0924A5),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray
            ),
            modifier = Modifier.scale(0.85f)
        )
    }
}

@Composable
fun ToggleItem(
    title: String,
    subtitle: String?,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (subtitle != null) Text(subtitle, color = Color.Gray, fontSize = 10.sp)
            }
            Switch(
                checked = isChecked, onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF0924A5), uncheckedThumbColor = Color.White, uncheckedTrackColor = Color.Gray)
            )
        }
    }
}
