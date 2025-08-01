package com.example.plantapp.domain.repositories

import com.example.plantapp.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): User?
    suspend fun register(name: String, email: String, password: String, phone: String): User?
    suspend fun resetPassword(email: String): Boolean
    fun getCurrentUser(): User?
    fun logout()
    suspend fun testFirebaseConnection(): Boolean
} 