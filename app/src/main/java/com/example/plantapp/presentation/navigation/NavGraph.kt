package com.example.plantapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.plantapp.presentation.screens.*
import com.example.plantapp.presentation.viewmodels.AuthViewModel
import com.example.plantapp.presentation.viewmodels.AuthState


@Composable
fun PlantAppNavGraph(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    
    val currentUser by authViewModel.currentUser.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    
    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                // Error handling is done in individual screens
            }
            else -> {}
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        
        composable(Screen.Login.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController, authViewModel = authViewModel)
        }
        
        composable(Screen.Home.route) {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }
        
        composable(Screen.Cart.route) {
            CartScreen(navController = navController, authViewModel = authViewModel)
        }
        
        composable(Screen.Checkout.route) {
            CheckoutScreen(navController = navController, authViewModel = authViewModel)
        }
        
        composable(Screen.Orders.route) {
            OrdersScreen(navController = navController, authViewModel = authViewModel)
        }
    }
} 