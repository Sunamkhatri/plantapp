package com.example.plantapp.data.repository

import com.example.plantapp.data.model.Order
import com.example.plantapp.data.model.OrderItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val ordersCollection = firestore.collection("orders")
    private val orderItemsCollection = firestore.collection("orderItems")
    
    fun getOrdersByUserId(userId: String): Flow<List<Order>> = flow {
        try {
            val snapshot = ordersCollection
                .whereEqualTo("userId", userId)
                .orderBy("orderDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Order::class.java)?.copy(id = doc.id)
            }
            emit(orders)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    suspend fun createOrder(order: Order): String {
        return try {
            val docRef = ordersCollection.add(order).await()
            docRef.id
        } catch (e: Exception) {
            ""
        }
    }
    
    suspend fun addOrderItems(orderItems: List<OrderItem>): Boolean {
        return try {
            orderItems.forEach { orderItem ->
                orderItemsCollection.add(orderItem).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
} 