package com.example.plantapp.data.repository

import com.example.plantapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")
    
    suspend fun login(email: String, password: String): User? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val userDoc = usersCollection.document(firebaseUser.uid).get().await()
                userDoc.toObject(User::class.java)?.copy(id = firebaseUser.uid)
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun register(name: String, email: String, password: String, phone: String): User? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val user = User(
                    id = firebaseUser.uid,
                    email = email,
                    name = name,
                    phone = phone
                )
                usersCollection.document(firebaseUser.uid).set(user).await()
                user
            }
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun getCurrentUser(): User? {
        return auth.currentUser?.let { firebaseUser ->
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: ""
            )
        }
    }
    
    fun logout() {
        auth.signOut()
    }
} 