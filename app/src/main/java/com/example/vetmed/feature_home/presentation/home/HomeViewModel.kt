package com.example.vetmed.feature_home.presentation.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.data.repository.Users
import com.example.vetmed.feature_animal.util.RequestState
import kotlinx.coroutines.launch


private const val TAG = "HomeViewModel"

class HomeViewModel(
) : ViewModel(
) {

    var vets: MutableState<Users> = mutableStateOf(RequestState.Idle)
    var isVet: MutableState<Boolean> = mutableStateOf(false)


    init {
        isVet()
        getAllVets()
    }

    private fun getAllVets() {
        viewModelScope.launch {
            MongoDB.getAllVets().collect { result ->
                vets.value = result
            }

        }
    }

    private fun isVet() {
        viewModelScope.launch {
            val result = try {
                MongoDB.isVet() // Assuming this is a suspend function
            } catch (e: Exception) {
                RequestState.Error(e)
            }

            when (result) {
                is RequestState.Success -> isVet.value = result.data
                is RequestState.Error -> Log.d("VetOrNot", "isVet: ${result.error.message}")
                else -> {}
            }
        }
    }




}
