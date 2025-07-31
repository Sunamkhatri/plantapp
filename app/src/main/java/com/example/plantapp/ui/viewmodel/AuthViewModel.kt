package com.example.plantapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantapp.data.model.User
import com.example.plantapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    
    private val userRepository = UserRepository()
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        checkCurrentUser()
    }
    
    private fun checkCurrentUser() {
        _currentUser.value = userRepository.getCurrentUser()
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = userRepository.login(email, password)
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success(user)
                } else {
                    _authState.value = AuthState.Error("Invalid email or password")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }
    
    fun register(name: String, email: String, password: String, phone: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = userRepository.register(name, email, password, phone)
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success(user)
                } else {
                    _authState.value = AuthState.Error("Registration failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }
    
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val success = userRepository.resetPassword(email)
                if (success) {
                    _authState.value = AuthState.PasswordResetSuccess
                } else {
                    _authState.value = AuthState.Error("Password reset failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Password reset failed")
            }
        }
    }
    
    fun logout() {
        userRepository.logout()
        _currentUser.value = null
        _authState.value = AuthState.Initial
    }
    
    fun clearError() {
        _authState.value = AuthState.Initial
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    object PasswordResetSuccess : AuthState()
} 