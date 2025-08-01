package com.example.plantapp.domain.entities

data class Plant(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val stockQuantity: Int = 0,
    val isAvailable: Boolean = true
) {
    // Required empty constructor for Firebase
    constructor() : this("", "", "", 0.0, "", "", 0, true)
} 