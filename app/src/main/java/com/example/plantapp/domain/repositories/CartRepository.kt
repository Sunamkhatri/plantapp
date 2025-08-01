package com.example.plantapp.domain.repositories

import com.example.plantapp.domain.entities.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItemsByUserId(userId: String): Flow<List<CartItem>>
    suspend fun addToCart(cartItem: CartItem): String
    suspend fun removeFromCart(cartItemId: String): Boolean
    suspend fun clearCart(userId: String): Boolean
    suspend fun getCartItemByUserAndPlant(userId: String, plantId: String): CartItem?
    suspend fun updateCartItemQuantity(cartItemId: String, quantity: Int): Boolean
} 