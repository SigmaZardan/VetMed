package com.example.vetmed.feature_home.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.data.repository.Users
import com.example.vetmed.feature_animal.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {

    var users: MutableState<Users> = mutableStateOf(RequestState.Idle)

    init {
        getAllVets()
    }

    private fun getAllVets() {
        viewModelScope.launch(Dispatchers.IO) {
            MongoDB.getAllVets().collect { result ->
                users.value = result
            }
        }
    }
}
