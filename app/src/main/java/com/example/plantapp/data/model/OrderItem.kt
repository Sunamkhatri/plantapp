package com.example.plantapp.data.model

data class OrderItem(
    val id: String = "",
    val orderId: String = "",
    val plantId: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
) 