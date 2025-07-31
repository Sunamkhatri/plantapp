package com.example.plantapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantapp.data.model.CartItem
import com.example.plantapp.data.model.CartItemWithPlant
import com.example.plantapp.data.model.Plant
import com.example.plantapp.data.repository.CartRepository
import com.example.plantapp.data.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val plantRepository: PlantRepository
) : ViewModel() {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _cartWithPlants = MutableStateFlow<List<CartItemWithPlant>>(emptyList())
    val cartWithPlants: StateFlow<List<CartItemWithPlant>> = _cartWithPlants.asStateFlow()
    
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()
    
    fun loadCartItems(userId: String) {
        viewModelScope.launch {
            cartRepository.getCartItemsByUserId(userId).collect { items ->
                _cartItems.value = items
                loadCartWithPlants(items)
            }
        }
    }
    
    private suspend fun loadCartWithPlants(cartItems: List<CartItem>) {
        val cartWithPlantsList = mutableListOf<CartItemWithPlant>()
        var total = 0.0
        
        for (cartItem in cartItems) {
            val plant = plantRepository.getPlantById(cartItem.plantId)
            if (plant != null) {
                cartWithPlantsList.add(CartItemWithPlant(cartItem, plant))
                total += plant.price * cartItem.quantity
            }
        }
        
        _cartWithPlants.value = cartWithPlantsList
        _totalAmount.value = total
    }
    
    fun addToCart(userId: String, plantId: String, quantity: Int = 1) {
        viewModelScope.launch {
            val existingItem = cartRepository.getCartItemByUserAndPlant(userId, plantId)
            if (existingItem != null) {
                val newQuantity = existingItem.quantity + quantity
                cartRepository.updateCartItemQuantity(existingItem.id, newQuantity)
            } else {
                val cartItem = CartItem(
                    userId = userId,
                    plantId = plantId,
                    quantity = quantity
                )
                cartRepository.addToCart(cartItem)
            }
        }
    }
    
    fun updateQuantity(cartItemId: String, quantity: Int) {
        viewModelScope.launch {
            if (quantity <= 0) {
                cartRepository.removeFromCart(cartItemId)
            } else {
                cartRepository.updateCartItemQuantity(cartItemId, quantity)
            }
        }
    }
    
    fun removeFromCart(cartItemId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(cartItemId)
        }
    }
    
    fun clearCart(userId: String) {
        viewModelScope.launch {
            cartRepository.clearCart(userId)
        }
    }
} 