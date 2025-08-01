package com.example.plantapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantapp.domain.repositories.UserRepository
import com.example.plantapp.domain.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _splashState = MutableStateFlow<SplashState>(SplashState.Initial)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkCurrentUser()
    }
    
    private fun checkCurrentUser() {
        _currentUser.value = userRepository.getCurrentUser()
    }
    
    fun checkFirebaseConnection() {
        viewModelScope.launch {
            _splashState.value = SplashState.Loading
            try {
                // Test Firebase connection by trying to access Firestore
                val testResult = userRepository.testFirebaseConnection()
                if (testResult) {
                    _splashState.value = SplashState.Success
                } else {
                    _splashState.value = SplashState.Error("Firebase connection failed")
                }
            } catch (e: Exception) {
                _splashState.value = SplashState.Error(e.message ?: "Connection error")
            }
        }
    }
    
    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}

sealed class SplashState {
    object Initial : SplashState()
    object Loading : SplashState()
    object Success : SplashState()
    data class Error(val message: String) : SplashState()
} 