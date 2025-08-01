package com.example.plantapp.data.repositories

import com.example.plantapp.domain.entities.Plant
import com.example.plantapp.domain.repositories.PlantRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlantRepository {
    
    private val plantsCollection = firestore.collection("plants")
    
    override fun getAllPlants(): Flow<List<Plant>> = flow {
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
    
    override suspend fun getPlantById(plantId: String): Plant? {
        return try {
            val doc = plantsCollection.document(plantId).get().await()
            doc.toObject(Plant::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }
    
    override fun getPlantsByCategory(category: String): Flow<List<Plant>> = flow {
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
    
    override suspend fun insertPlant(plant: Plant): String {
        return try {
            val docRef = plantsCollection.add(plant).await()
            docRef.id
        } catch (e: Exception) {
            ""
        }
    }
    
    override suspend fun updateStockQuantity(plantId: String, quantity: Int): Boolean {
        return try {
            plantsCollection.document(plantId).update("stockQuantity", quantity).await()
            true
        } catch (e: Exception) {
            false
        }
    }
} 