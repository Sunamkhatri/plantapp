package com.example.plantapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantapp.data.model.*
import com.example.plantapp.data.repository.CartRepository
import com.example.plantapp.data.repository.OrderRepository
import com.example.plantapp.data.repository.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {
    
    private val orderRepository = OrderRepository()
    private val cartRepository = CartRepository()
    private val plantRepository = PlantRepository()
    
    private val _orderState = MutableStateFlow<OrderState>(OrderState.Initial)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()
    
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    
    fun createOrder(
        userId: String,
        customerName: String,
        customerAddress: String,
        customerPhone: String,
        notes: String,
        cartItems: List<CartItemWithPlant>
    ) {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading
            try {
                val totalAmount = cartItems.sumOf { it.plant.price * it.cartItem.quantity }
                
                val order = Order(
                    userId = userId,
                    customerName = customerName,
                    customerAddress = customerAddress,
                    customerPhone = customerPhone,
                    notes = notes,
                    totalAmount = totalAmount
                )
                
                val orderId = orderRepository.createOrder(order)
                
                if (orderId.isNotEmpty()) {
                    val orderItems = cartItems.map { cartItemWithPlant ->
                        OrderItem(
                            orderId = orderId,
                            plantId = cartItemWithPlant.plant.id,
                            quantity = cartItemWithPlant.cartItem.quantity,
                            price = cartItemWithPlant.plant.price
                        )
                    }
                    
                    orderRepository.addOrderItems(orderItems)
                    
                    cartItems.forEach { cartItemWithPlant ->
                        val currentStock = cartItemWithPlant.plant.stockQuantity
                        val newStock = currentStock - cartItemWithPlant.cartItem.quantity
                        plantRepository.updateStockQuantity(cartItemWithPlant.plant.id, newStock)
                    }
                    
                    cartRepository.clearCart(userId)
                    
                    _orderState.value = OrderState.Success(order.copy(id = orderId))
                } else {
                    _orderState.value = OrderState.Error("Order creation failed")
                }
            } catch (e: Exception) {
                _orderState.value = OrderState.Error(e.message ?: "Order creation failed")
            }
        }
    }
    
    fun loadOrders(userId: String) {
        viewModelScope.launch {
            orderRepository.getOrdersByUserId(userId).collect { ordersList ->
                _orders.value = ordersList
            }
        }
    }
    
    fun clearOrderState() {
        _orderState.value = OrderState.Initial
    }
}

sealed class OrderState {
    object Initial : OrderState()
    object Loading : OrderState()
    data class Success(val order: Order) : OrderState()
    data class Error(val message: String) : OrderState()
} 