package com.example.lapaksoed.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapaksoed.R
import com.example.lapaksoed.data.remote.ApiClient
import com.example.lapaksoed.data.remote.MessageResponse
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit
) {
    val detailState by viewModel.detailState.collectAsState()
    val activeConv by viewModel.activeConversation.collectAsState()
    
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val yellowBtn = Color(0xFFFFD600)
    val textBlue = Color(0xFF0924A5)

    LaunchedEffect(detailState) {
        if (detailState is ChatDetailState.Success) {
            val size = (detailState as ChatDetailState.Success).messages.size
            if (size > 0) {
                listState.animateScrollToItem(size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            // Header
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
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.clickable { onBack() }.padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF00FF55), CircleShape)
                            .border(1.dp, Color.Black, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = activeConv?.otherUserName ?: "",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // Blue checkmark badge (dummy)
                            Box(modifier = Modifier.size(14.dp).background(Color.Blue, CircleShape))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFF00FF55), CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Online", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu")
                }
            }
        },
        bottomBar = {
            // Message Composer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
                    .border(2.dp, yellowBtn, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // + button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
                            .clickable { /* noop */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.Gray)
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // TextField
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        BasicTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (inputText.isEmpty()) {
                                    Text("Ketik pesan untuk penjual...", color = Color.Gray, fontSize = 14.sp)
                                }
                                innerTextField()
                            }
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Send Button
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(yellowBtn, RoundedCornerShape(12.dp))
                            .clickable {
                                if (inputText.isNotBlank() && activeConv != null) {
                                    viewModel.sendMessage(activeConv!!.id, inputText)
                                    inputText = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.Black, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    } { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Image(
                painter = painterResource(id = R.drawable.bg_home),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            when (detailState) {
                is ChatDetailState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = yellowBtn)
                    }
                }
                is ChatDetailState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text((detailState as ChatDetailState.Error).error, color = Color.White)
                    }
                }
                is ChatDetailState.Success -> {
                    val messages = (detailState as ChatDetailState.Success).messages
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(messages) { msg ->
                            // Check if current user
                            val isMe = msg.senderId.toString() == ApiClient.currentUserId
                            MessageBubble(msg, isMe)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun MessageBubble(msg: MessageResponse, isMe: Boolean) {
    val yellowBubble = Color(0xFFFFD600)
    val whiteBubble = Color.White
    
    val timeStr = try {
        val instant = Instant.parse(msg.createdAt)
        val formatter = DateTimeFormatter.ofPattern("HH.mm").withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        "00.00"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isMe) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFF00FF55), CircleShape)
                    .border(1.dp, Color.Black, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    if (isMe) yellowBubble else whiteBubble,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 0.dp,
                        bottomEnd = if (isMe) 0.dp else 16.dp
                    )
                )
                .border(
                    1.dp,
                    if (isMe) Color.Transparent else Color.LightGray,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isMe) 16.dp else 0.dp,
                        bottomEnd = if (isMe) 0.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Column {
                if (!isMe) {
                    Text(
                        text = "Admin ${msg.senderName}",
                        color = Color(0xFF0924A5),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                Text(
                    text = msg.body,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeStr,
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                    if (isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("✓✓", color = Color.Blue, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
