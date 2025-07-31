package com.example.plantapp.data.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

object FirebaseUtil {
    
    fun <T> getCollectionAsFlow(
        firestore: FirebaseFirestore,
        collection: String,
        transform: (Map<String, Any>, String) -> T
    ): Flow<List<T>> = flow {
        try {
            val snapshot = firestore.collection(collection).get().await()
            val items = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    transform(data, doc.id)
                }
            }
            emit(items)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    fun <T> getCollectionWithQueryAsFlow(
        firestore: FirebaseFirestore,
        collection: String,
        query: (Query) -> Query,
        transform: (Map<String, Any>, String) -> T
    ): Flow<List<T>> = flow {
        try {
            val snapshot = query(firestore.collection(collection)).get().await()
            val items = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    transform(data, doc.id)
                }
            }
            emit(items)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
    
    suspend fun <T> addDocument(
        firestore: FirebaseFirestore,
        collection: String,
        data: T
    ): String {
        return try {
            val docRef = firestore.collection(collection).add(data as Any).await()
            docRef.id
        } catch (e: Exception) {
            ""
        }
    }
    
    suspend fun updateDocument(
        firestore: FirebaseFirestore,
        collection: String,
        documentId: String,
        updates: Map<String, Any>
    ): Boolean {
        return try {
            firestore.collection(collection).document(documentId).update(updates).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun deleteDocument(
        firestore: FirebaseFirestore,
        collection: String,
        documentId: String
    ): Boolean {
        return try {
            firestore.collection(collection).document(documentId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
} 