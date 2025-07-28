package com.example.plantapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.plantapp.ui.components.PlantCard
import com.example.plantapp.ui.navigation.Screen
import com.example.plantapp.ui.viewmodel.AuthViewModel
import com.example.plantapp.ui.viewmodel.CartViewModel
import com.example.plantapp.ui.viewmodel.PlantListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    plantListViewModel: PlantListViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val plants by plantListViewModel.plants.collectAsState()
    val selectedCategory by plantListViewModel.selectedCategory.collectAsState()
    val isLoading by plantListViewModel.isLoading.collectAsState()
    val error by plantListViewModel.error.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    val categories = listOf("All", "Indoor", "Outdoor", "Succulents")
    
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            cartViewModel.loadCartItems(user.id)
        }
    }
    
    LaunchedEffect(error) {
        if (error != null) {
            // Show error message
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Plant Store",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                    IconButton(onClick = { navController.navigate(Screen.Orders.route) }) {
                        Icon(Icons.Default.List, contentDescription = "Orders")
                    }
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category Filter
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = if (category == "All") selectedCategory == null else selectedCategory == category
                    FilterChip(
                        onClick = {
                            plantListViewModel.filterByCategory(if (category == "All") null else category)
                        },
                        label = { Text(category) },
                        selected = isSelected,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error loading plants",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { 
                                plantListViewModel.clearError()
                                plantListViewModel.loadPlants()
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(plants) { plant ->
                        PlantCard(
                            plant = plant,
                            onAddToCart = {
                                currentUser?.let { user ->
                                    cartViewModel.addToCart(user.id, plant.id)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
} 