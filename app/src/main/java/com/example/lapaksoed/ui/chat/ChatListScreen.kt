package com.example.lapaksoed.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapaksoed.R
import com.example.lapaksoed.data.remote.ConversationResponse
import com.example.lapaksoed.ui.home.BottomNavigationBar
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    viewModel: ChatViewModel,
    onNavigateToDetail: () -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val listState by viewModel.listState.collectAsState()
    val yellowBtn = Color(0xFFFFD600)
    val blueBg = Color(0xFF0924A5)

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                activeColor = yellowBtn, 
                bgColor = blueBg, 
                activeRoute = "chat",
                onNavigate = { route -> 
                    when (route) {
                        "home" -> onNavigateToHome()
                        "profile" -> onNavigateToProfile()
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

            Column(modifier = Modifier.fillMaxSize()) {
                // Header Top Shape (Figma: Header with rounded curve, but the image shows white header for the whole page or just floating container)
                // Actually the whole chat list / empty chat is inside a big white container.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .background(Color.White, RoundedCornerShape(32.dp))
                        .border(4.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .clip(RoundedCornerShape(32.dp))
                ) {
                    when (listState) {
                        is ChatListState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = yellowBtn)
                            }
                        }
                        is ChatListState.Error -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text((listState as ChatListState.Error).error, color = Color.Red)
                            }
                        }
                        is ChatListState.Success -> {
                            val conversations = (listState as ChatListState.Success).conversations
                            if (conversations.isEmpty()) {
                                EmptyChatView()
                            } else {
                                ChatListView(
                                    conversations = conversations,
                                    onChatClick = {
                                        viewModel.selectConversation(it)
                                        onNavigateToDetail()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyChatView() {
    val yellowBtn = Color(0xFFFFD600)
    val textBlue = Color(0xFF0924A5)

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Chat Icon placeholder (yellow square with chat bubble)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(yellowBtn, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Draw a simple chat outline or just use a text icon
            Icon(
                painter = painterResource(android.R.drawable.stat_notify_chat), // standard icon or just text
                contentDescription = null,
                tint = textBlue,
                modifier = Modifier.size(50.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Belum Ada Obrolan",
            color = textBlue,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Layanan chat akan terbuka otomatis—lengkap dengan pesan salam otomatis—begitu pesanan dikonfirmasi oleh penjual dan driver.",
            color = textBlue.copy(alpha = 0.8f),
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun ChatListView(
    conversations: List<ConversationResponse>,
    onChatClick: (ConversationResponse) -> Unit
) {
    val yellowBtn = Color(0xFFFFD600)
    
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(modifier = Modifier.weight(1f), color = yellowBtn, thickness = 2.dp)
            Text(" Chat ", color = Color.Black, fontWeight = FontWeight.Bold)
            Divider(modifier = Modifier.weight(1f), color = yellowBtn, thickness = 2.dp)
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(conversations) { conv ->
                Card(
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onChatClick(conv) }
                        .border(2.dp, yellowBtn, RoundedCornerShape(32.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFF00FF55), CircleShape)
                                .border(1.dp, Color.Black, CircleShape)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = conv.otherUserName,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mengenai: ${conv.listingTitle}",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        
                        Column(horizontalAlignment = Alignment.End) {
                            val timeStr = try {
                                val instant = Instant.parse(conv.lastMessageAt)
                                val formatter = DateTimeFormatter.ofPattern("MMM dd").withZone(ZoneId.systemDefault())
                                formatter.format(instant)
                            } catch (e: Exception) {
                                "Aug 20"
                            }
                            Text(text = timeStr, color = Color.Gray, fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            // Unread indicator is mocked because backend doesn't provide it yet
                            // Per instruction: "Jika backend menyediakan informasi unread message: Tampilkan... Jika backend mendukung read status... Jangan membuat sistem read/unread baru jika backend sudah menyediakan."
                            // But backend DOES NOT provide it in ConversationResponse. So we omit it to stick to real data.
                        }
                    }
                }
            }
        }
    }
}
