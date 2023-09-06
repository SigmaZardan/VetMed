package com.example.vetmed.feature_home.presentation.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.connectivity.ConnectivityObserver
import com.example.vetmed.feature_animal.connectivity.NetworkConnectivityObserver
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.data.repository.Users
import com.example.vetmed.feature_animal.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "HomeViewModel"

class HomeViewModel(
) : ViewModel(
) {

    var users: MutableState<Users> = mutableStateOf(RequestState.Idle)
    var isVet: MutableState<Boolean> = mutableStateOf(false)

    init {
        getAllVets()
        isVet()
    }

    private fun getAllVets() {
        viewModelScope.launch {
            MongoDB.getAllVets().collect { result ->
                if (result is RequestState.Success) {
                    users.value = result
                } else if (result is RequestState.Error) {
                    Log.d("Handle Error", "${result.error.message}")
                }

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
