package com.example.plantapp.data.model

import java.util.Date

data class Order(
    val id: String = "",
    val userId: String = "",
    val customerName: String = "",
    val customerAddress: String = "",
    val customerPhone: String = "",
    val notes: String = "",
    val totalAmount: Double = 0.0,
    val orderDate: Date = Date(),
    val status: OrderStatus = OrderStatus.PENDING
)

enum class OrderStatus {
    PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
} 