package com.example.plantapp.data.repositories

import com.example.plantapp.domain.entities.User
import com.example.plantapp.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {
    
    private val usersCollection = firestore.collection("users")
    
    override suspend fun login(email: String, password: String): User? {
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
    
    override suspend fun register(name: String, email: String, password: String, phone: String): User? {
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
    
    override suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun getCurrentUser(): User? {
        return auth.currentUser?.let { firebaseUser ->
            User(
                id = firebaseUser.uid,
                email = firebaseUser.email ?: "",
                name = firebaseUser.displayName ?: ""
            )
        }
    }
    
    override fun logout() {
        auth.signOut()
    }
    
    override suspend fun testFirebaseConnection(): Boolean {
        return try {
            // Test Firestore connection by trying to read a document
            firestore.collection("test").document("connection").get().await()
            true
        } catch (e: Exception) {
            // If the test document doesn't exist, that's fine - we just want to test the connection
            // If there's a network error, it will throw an exception
            e.message?.contains("network") == false
        }
    }
} 