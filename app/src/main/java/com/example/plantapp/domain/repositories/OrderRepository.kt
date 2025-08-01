package com.example.plantapp.domain.repositories

import com.example.plantapp.domain.entities.Order
import com.example.plantapp.domain.entities.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrdersByUserId(userId: String): Flow<List<Order>>
    suspend fun createOrder(order: Order): String
    suspend fun addOrderItems(orderItems: List<OrderItem>): Boolean
} 