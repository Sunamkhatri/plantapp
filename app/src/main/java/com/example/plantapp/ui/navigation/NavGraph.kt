package com.example.plantapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.plantapp.ui.screens.*
import com.example.plantapp.ui.viewmodel.AuthViewModel
import com.example.plantapp.ui.viewmodel.AuthState

@Composable
fun PlantAppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }
        
        composable(Screen.Checkout.route) {
            CheckoutScreen(navController = navController)
        }
        
        composable(Screen.Orders.route) {
            OrdersScreen(navController = navController)
        }
    }
    
    when (authState) {
        is AuthState.Success -> {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
        else -> {}
    }
} 