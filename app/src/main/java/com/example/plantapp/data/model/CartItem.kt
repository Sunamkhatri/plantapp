package com.example.plantapp.data.model

data class CartItem(
    val id: String = "",
    val plantId: String = "",
    val quantity: Int = 0,
    val userId: String = ""
) {
    // Required empty constructor for Firebase
    constructor() : this("", "", 0, "")
} 