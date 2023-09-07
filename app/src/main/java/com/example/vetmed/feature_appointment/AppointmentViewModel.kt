package com.example.vetmed.feature_appointment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.data.repository.Users
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User
import kotlinx.coroutines.launch

class AppointmentViewModel : ViewModel() {
    var users: MutableState<Users> = mutableStateOf(RequestState.Idle)
    var filteredUser: List<User> = mutableStateListOf()
    init {
        getAllUsers()
    }

    private fun getAllUsers() {
        viewModelScope.launch {
            MongoDB.getAllUsers().collect {
                users.value = it
            }
        }
    }
}