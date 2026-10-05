package com.example.lapaksoed.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lapaksoed.ui.auth.LoginScreen
import com.example.lapaksoed.ui.auth.RegisterScreen
import com.example.lapaksoed.ui.home.HomeScreen
import com.example.lapaksoed.ui.order.OrderViewModel
import com.example.lapaksoed.ui.order.OrderSummaryScreen
import com.example.lapaksoed.ui.order.PaymentScreen
import com.example.lapaksoed.ui.order.SelectProductScreen
import com.example.lapaksoed.ui.chat.ChatViewModel
import com.example.lapaksoed.ui.chat.ChatListScreen
import com.example.lapaksoed.ui.chat.ChatDetailScreen
import com.example.lapaksoed.ui.profile.ProfileScreen
import com.example.lapaksoed.ui.profile.ProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val orderViewModel: OrderViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { 
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                } },
                onRegisterSuccess = { 
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToChat = { navController.navigate("chat_list") },
                onProductClick = { product ->
                    orderViewModel.setListing(product)
                    navController.navigate("select_product")
                }
            )
        }
        composable("chat_list") {
            ChatListScreen(
                viewModel = chatViewModel,
                onNavigateToDetail = { navController.navigate("chat_detail") },
                onNavigateToHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }
        composable("chat_detail") {
            ChatDetailScreen(
                viewModel = chatViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("select_product") {
            SelectProductScreen(
                viewModel = orderViewModel,
                onNext = { navController.navigate("order_summary") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("order_summary") {
            OrderSummaryScreen(
                viewModel = orderViewModel,
                onNext = { navController.navigate("payment") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("payment") {
            PaymentScreen(
                viewModel = orderViewModel,
                onPay = { 
                    // To be integrated with backend later
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(
                viewModel = profileViewModel,
                onNavigateToHome = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                onNavigateToOrders = { /* TODO if orders list screen is created */ },
                onNavigateToChat = { navController.navigate("chat_list") },
                onLogout = { navController.navigate("login") { popUpTo(0) { inclusive = true } } }
            )
        }
    }
}
