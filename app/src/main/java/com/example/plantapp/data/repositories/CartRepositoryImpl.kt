package com.example.plantapp.data.repositories

import com.example.plantapp.domain.entities.CartItem
import com.example.plantapp.domain.repositories.CartRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CartRepository {
    
    private val cartCollection = firestore.collection("cart")
    
    override fun getCartItemsByUserId(userId: String): Flow<List<CartItem>> = flow {
        try {
            val snapshot = cartCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            val cartItems = snapshot.documents.mapNotNull { doc ->
                doc.toObject(CartItem::class.java)?.copy(id = doc.id)
            }
            emit(cartItems)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    override suspend fun addToCart(cartItem: CartItem): String {
        return try {
            val docRef = cartCollection.add(cartItem).await()
            docRef.id
        } catch (e: Exception) {
            ""
        }
    }
    
    override suspend fun removeFromCart(cartItemId: String): Boolean {
        return try {
            cartCollection.document(cartItemId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun clearCart(userId: String): Boolean {
        return try {
            val snapshot = cartCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            snapshot.documents.forEach { doc ->
                doc.reference.delete().await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun getCartItemByUserAndPlant(userId: String, plantId: String): CartItem? {
        return try {
            val snapshot = cartCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("plantId", plantId)
                .get()
                .await()
            
            snapshot.documents.firstOrNull()?.let { doc ->
                doc.toObject(CartItem::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun updateCartItemQuantity(cartItemId: String, quantity: Int): Boolean {
        return try {
            cartCollection.document(cartItemId).update("quantity", quantity).await()
            true
        } catch (e: Exception) {
            false
        }
    }
} 