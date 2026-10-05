package com.example.lapaksoed.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items as lazyRowItems
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lapaksoed.R
import com.example.lapaksoed.data.remote.ListingResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToProfile: () -> Unit = {},
    onProductClick: (ListingResponse) -> Unit = {},
    onNavigateToChat: () -> Unit = {}
) {
    val homeState by viewModel.homeState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val isSearchActive by viewModel.isSearchActive.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val blueBg = Color(0xFF0924A5)
    val yellowBtn = Color(0xFFFFD600)
    val cardBg = Color.White
    val textBlue = Color(0xFF0924A5)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                activeColor = yellowBtn, 
                bgColor = blueBg, 
                activeRoute = "home",
                onNavigate = { route ->
                    when (route) {
                        "chat" -> onNavigateToChat()
                        "profile" -> onNavigateToProfile()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_home),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header (White background, rounded bottom, yellow border)
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
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(yellowBtn, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("LS", color = blueBg, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Iklan Berjalan (Banner Carousel) di bagian paling atas
                        Box(modifier = Modifier.weight(1f)) {
                            BannerCarousel()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar Box to handle overlap
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).zIndex(1f)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { viewModel.updateSearchQuery(it) },
                            onSearch = { viewModel.performSearch(it) },
                            onFocusChange = { viewModel.setSearchActive(it) }
                        )

                        if (isSearchActive && recommendations.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                    recommendations.forEach { recommendation ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    viewModel.performSearch(recommendation)
                                                }
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = null,
                                                tint = Color.Gray,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = recommendation,
                                                color = Color.Black,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content Area
                if (searchQuery.isNotEmpty() && isSearchActive) {
                    // Empty state while typing if no recommendations
                    if (recommendations.isEmpty()) {
                       // Could show "Press enter to search"
                    }
                } else {
                    // Normal Home Content
                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                        // Category Box
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    CategoryItem("Makanan", Icons.Outlined.Restaurant, selectedCategory) { viewModel.filterByCategory(it) }
                                    CategoryItem("Minuman", Icons.Outlined.LocalDrink, selectedCategory) { viewModel.filterByCategory(it) }
                                    CategoryItem("Pakaian", Icons.Outlined.Checkroom, selectedCategory) { viewModel.filterByCategory(it) }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    CategoryItem("Barang", Icons.Outlined.Inventory2, selectedCategory) { viewModel.filterByCategory(it) }
                                    CategoryItem("Jasa Service", Icons.Outlined.Build, selectedCategory) { viewModel.filterByCategory(it) }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Product List
                        when (homeState) {
                            is HomeState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = yellowBtn)
                                }
                            }
                            is HomeState.Error -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = (homeState as HomeState.Error).message,
                                        color = Color.White
                                    )
                                }
                            }
                            is HomeState.Success -> {
                                val products = (homeState as HomeState.Success).products
                                if (products.isEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Tidak ada produk ditemukan.",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(bottom = 16.dp)
                                    ) {
                                        items(products) { product ->
                                            ProductCard(product, yellowBtn, textBlue, onProductClick)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White, RoundedCornerShape(25.dp))
            .border(2.dp, Color(0xFFD3D3D3), RoundedCornerShape(25.dp))
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        onFocusChange(focusState.isFocused)
                    },
                textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text("Search...", color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            )
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = Color.Gray,
                    modifier = Modifier.clickable {
                        onQueryChange("")
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    title: String, 
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selectedCategory: String?,
    onClick: (String) -> Unit
) {
    val textBlue = Color(0xFF0924A5)
    val isSelected = selectedCategory == title
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(if (isSelected) 3.dp else 1.dp, if (isSelected) Color(0xFFFFD600) else Color.Black, RoundedCornerShape(12.dp))
            .clickable { onClick(title) }
            .padding(8.dp)
            .width(80.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = textBlue,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = textBlue,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun BannerCarousel() {
    val banners = listOf("IKLAN", "PROMO SPESIAL", "GRATIS ONGKIR")
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        lazyRowItems(banners) { banner ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable { /* TODO: API integration later */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = banner,
                    color = Color(0xFFFFD600), // Yellow/Orange color matching Figma "IKLAN"
                    fontSize = 36.sp,
                    fontFamily = FontFamily.Cursive,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ListingResponse, 
    yellowBtn: Color, 
    textBlue: Color, 
    onClick: (ListingResponse) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick(product) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.LightGray)
            ) {
                // Image placeholder
                // Assuming imageUrls is empty, we show a gray box.
                // You can replace this with Coil or Glide Image if needed.
                
                // Promo badge
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Red, RoundedCornerShape(8.dp)) // Approximate jagged badge with a simple rounded box for now
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PROMO !",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(yellowBtn, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "5.0", // Dummy rating as backend doesn't have it
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rp ${product.price.toLong()}",
                    color = textBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    activeColor: Color, 
    bgColor: Color, 
    activeRoute: String = "home",
    onNavigate: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(32.dp))
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            icon = Icons.Default.Home, 
            isActive = activeRoute == "home", 
            activeColor = activeColor,
            onClick = { onNavigate("home") }
        )
        BottomNavItem(
            icon = Icons.Default.Schedule, 
            isActive = activeRoute == "orders", 
            activeColor = activeColor,
            onClick = { onNavigate("orders") }
        )
        BottomNavItem(
            icon = Icons.Default.Chat, 
            isActive = activeRoute == "chat", 
            activeColor = activeColor,
            onClick = { onNavigate("chat") }
        )
        BottomNavItem(
            icon = Icons.Default.Person, 
            isActive = activeRoute == "profile", 
            activeColor = activeColor,
            onClick = { onNavigate("profile") }
        )
    }
}

@Composable
fun BottomNavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .background(
                if (isActive) activeColor else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(28.dp)
        )
    }
}
