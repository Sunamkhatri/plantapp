package com.example.plantapp.domain.entities

data class OrderItem(
    val id: String = "",
    val orderId: String = "",
    val plantId: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0
) {
    // Required empty constructor for Firebase
    constructor() : this("", "", "", 0, 0.0)
} 