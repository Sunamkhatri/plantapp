package com.example.plantapp.domain.entities

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = ""
) {
    // Required empty constructor for Firebase
    constructor() : this("", "", "", "", "")
} 