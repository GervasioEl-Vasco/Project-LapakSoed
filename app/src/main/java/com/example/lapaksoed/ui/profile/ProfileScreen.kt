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
    onNavigateToHome: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToChat: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    
    var isNotificationEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("notif_enabled", false)) }
    var isDarkThemeEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("dark_theme", false)) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val yellowBtn = Color(0xFFFFD600)
    val blueBg = Color(0xFF0924A5)
    val avatarColor = Color(0xFF6B3A36) // Dark brown/reddish color from Figma

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
                // Header (Same as Home: White background, rounded bottom, yellow border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(
                            Color.White,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar in Header
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(avatarColor, CircleShape)
                                .border(1.dp, Color.Black, CircleShape)
                        )
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

                // Profile Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color(0xFF5E0B0B), CircleShape) // Darker red/brown for the big avatar
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = userProfile?.fullName ?: "Memuat...",
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = userProfile?.email ?: "",
                                color = Color(0xFFE5A822), // Orange/yellow
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Google Chip
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF0D328C), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Placeholder for G icon, using a simple text G
                                    Box(
                                        modifier = Modifier.size(16.dp).background(Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("G", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Terhubung via Google", color = Color.White, fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Menus
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        ProfileMenuItem(icon = Icons.Outlined.Settings, title = "Pengaturan Akun")
                        Divider(color = Color.LightGray)
                        ProfileMenuItem(icon = Icons.Outlined.Person, title = "Update Profil")
                        Divider(color = Color.LightGray)
                        ProfileMenuItem(icon = Icons.Outlined.HelpOutline, title = "Pusat Bantuan")
                        Divider(color = Color.LightGray)
                        ProfileMenuItem(icon = Icons.Outlined.Description, title = "Ketentuan dan Privasi")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Daftar Mitra Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(60.dp)
                        .background(Color(0xFFF29B00), RoundedCornerShape(16.dp)) // Orange background
                        .clickable { /* noop */ },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AssignmentTurnedIn, // Approximating icon
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Daftar Sebagai Mitra",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Notifikasi HP
                ToggleItem(
                    title = "Notifikasi HP",
                    subtitle = "Ketuk untuk mengaktifkan",
                    icon = Icons.Outlined.NotificationsOff,
                    isChecked = isNotificationEnabled,
                    onCheckedChange = { 
                        isNotificationEnabled = it
                        sharedPrefs.edit().putBoolean("notif_enabled", it).apply()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tema Aplikasi
                ToggleItem(
                    title = "Tema Aplikasi",
                    subtitle = null,
                    icon = Icons.Outlined.DarkMode,
                    isChecked = isDarkThemeEnabled,
                    onCheckedChange = { 
                        isDarkThemeEnabled = it
                        sharedPrefs.edit().putBoolean("dark_theme", it).apply()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(60.dp)
                        .background(Color(0xFFFFEAEA), RoundedCornerShape(16.dp)) // Light red tint
                        .border(2.dp, Color.Red, RoundedCornerShape(16.dp))
                        .clickable {
                            viewModel.logout()
                            onLogout()
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = "Log Out",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "LOG OUT",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* noop */ }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Black
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF0924A5), // Blue when active
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Gray
                )
            )
        }
    }
}
