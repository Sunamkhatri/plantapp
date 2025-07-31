package com.example.plantapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantapp.data.model.Plant
import com.example.plantapp.data.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {
    
    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: StateFlow<List<Plant>> = _plants.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadPlants()
        insertSamplePlants()
    }
    
    fun loadPlants() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                plantRepository.getAllPlants().collect { plantsList ->
                    _plants.value = plantsList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error
            }
        }
    }
    
    fun filterByCategory(category: String?) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (category != null) {
                    plantRepository.getPlantsByCategory(category).collect { plantsList ->
                        _plants.value = plantsList
                        _isLoading.value = false
                    }
                } else {
                    plantRepository.getAllPlants().collect { plantsList ->
                        _plants.value = plantsList
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error
            }
        }
    }
    
    private fun insertSamplePlants() {
        viewModelScope.launch {
            val samplePlants = listOf(
                Plant(
                    name = "Monstera Deliciosa",
                    description = "Beautiful tropical plant with distinctive leaf holes and splits",
                    price = 2500.0,
                    imageUrl = "https://images.unsplash.com/photo-1614594975525-e45190c55d0b?w=400",
                    category = "Indoor",
                    stockQuantity = 10
                ),
                Plant(
                    name = "Snake Plant",
                    description = "Low maintenance plant perfect for beginners",
                    price = 1200.0,
                    imageUrl = "https://images.unsplash.com/photo-1593691509543-c55fb32e5cee?w=400",
                    category = "Indoor",
                    stockQuantity = 15
                ),
                Plant(
                    name = "Peace Lily",
                    description = "Elegant flowering plant that purifies indoor air",
                    price = 1800.0,
                    imageUrl = "https://images.unsplash.com/photo-1593691509543-c55fb32e5cee?w=400",
                    category = "Indoor",
                    stockQuantity = 8
                ),
                Plant(
                    name = "Rose Bush",
                    description = "Classic flowering shrub with beautiful blooms",
                    price = 800.0,
                    imageUrl = "https://images.unsplash.com/photo-1593691509543-c55fb32e5cee?w=400",
                    category = "Outdoor",
                    stockQuantity = 20
                ),
                Plant(
                    name = "Lavender",
                    description = "Aromatic herb with purple flowers",
                    price = 600.0,
                    imageUrl = "https://images.unsplash.com/photo-1593691509543-c55fb32e5cee?w=400",
                    category = "Outdoor",
                    stockQuantity = 25
                ),
                Plant(
                    name = "Succulent Collection",
                    description = "Set of 5 different colorful succulents",
                    price = 1500.0,
                    imageUrl = "https://images.unsplash.com/photo-1593691509543-c55fb32e5cee?w=400",
                    category = "Succulents",
                    stockQuantity = 12
                )
            )
            
            samplePlants.forEach { plant ->
                plantRepository.insertPlant(plant)
            }
        }
    }
} 