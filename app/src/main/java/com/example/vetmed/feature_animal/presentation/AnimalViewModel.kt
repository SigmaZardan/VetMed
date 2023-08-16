package com.example.vetmed.feature_animal.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.Animals
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.util.RequestState
import kotlinx.coroutines.launch

class AnimalViewModel : ViewModel() {
    var animals: MutableState<Animals> = mutableStateOf(RequestState.Idle)

    init {
        observeAllAnimals()
    }

    private fun observeAllAnimals() {
        viewModelScope.launch {
            MongoDB.getAllAnimals().collect { result ->
                animals.value = result
            }
        }

    }
}