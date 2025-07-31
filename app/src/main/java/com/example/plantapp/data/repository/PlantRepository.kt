package com.example.plantapp.data.repository

import com.example.plantapp.data.model.Plant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PlantRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val plantsCollection = firestore.collection("plants")
    
    fun getAllPlants(): Flow<List<Plant>> = flow {
        try {
            val snapshot = plantsCollection
                .whereEqualTo("isAvailable", true)
                .get()
                .await()
            
            val plants = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Plant::class.java)?.copy(id = doc.id)
            }
            emit(plants)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    suspend fun getPlantById(plantId: String): Plant? {
        return try {
            val doc = plantsCollection.document(plantId).get().await()
            doc.toObject(Plant::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
    
    fun getPlantsByCategory(category: String): Flow<List<Plant>> = flow {
        try {
            val snapshot = plantsCollection
                .whereEqualTo("category", category)
                .whereEqualTo("isAvailable", true)
                .get()
                .await()
            
            val plants = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Plant::class.java)?.copy(id = doc.id)
            }
            emit(plants)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    suspend fun insertPlant(plant: Plant): String {
        return try {
            val docRef = plantsCollection.add(plant).await()
            docRef.id
        } catch (e: Exception) {
            ""
        }
    }
    
    suspend fun updateStockQuantity(plantId: String, quantity: Int): Boolean {
        return try {
            plantsCollection.document(plantId).update("stockQuantity", quantity).await()
            true
        } catch (e: Exception) {
            false
        }
    }
} 