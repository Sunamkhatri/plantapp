package com.example.plantapp.domain.repositories

import com.example.plantapp.domain.entities.Plant
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    fun getAllPlants(): Flow<List<Plant>>
    suspend fun getPlantById(plantId: String): Plant?
    fun getPlantsByCategory(category: String): Flow<List<Plant>>
    suspend fun insertPlant(plant: Plant): String
    suspend fun updateStockQuantity(plantId: String, quantity: Int): Boolean
} 