package com.example.plantapp.data.util

sealed class NetworkState<out T> {
    object Loading : NetworkState<Nothing>()
    data class Success<T>(val data: T) : NetworkState<T>()
    data class Error(val message: String) : NetworkState<Nothing>()
    
    fun isLoading(): Boolean = this is Loading
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    
    fun getDataOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    fun getError(): String? = when (this) {
        is Error -> message
        else -> null
    }
}

inline fun <T> NetworkState<T>.onLoading(action: () -> Unit): NetworkState<T> {
    if (this is NetworkState.Loading) action()
    return this
}

inline fun <T> NetworkState<T>.onSuccess(action: (T) -> Unit): NetworkState<T> {
    if (this is NetworkState.Success) action(data)
    return this
}

inline fun <T> NetworkState<T>.onError(action: (String) -> Unit): NetworkState<T> {
    if (this is NetworkState.Error) action(message)
    return this
} 