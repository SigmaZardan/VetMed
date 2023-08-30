package com.example.vetmed.feature_animal.presentation


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.connectivity.ConnectivityObserver
import com.example.vetmed.feature_animal.data.repository.Animals
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_animal.connectivity.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject


@HiltViewModel
class AnimalViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver
) : ViewModel() {
    private lateinit var allAnimalsJob: Job
    private lateinit var filteredAnimalsJob: Job

    var animals: MutableState<Animals> = mutableStateOf(RequestState.Idle)
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
    var dateIsSelected by mutableStateOf(false)
        private set

    init {
        getAnimals()
        viewModelScope.launch {
            connectivity.observe().collect { network = it }
        }
    }

    fun getAnimals(zonedDateTime: ZonedDateTime? = null) {
        dateIsSelected = zonedDateTime != null
        animals.value = RequestState.Loading
        if (dateIsSelected && zonedDateTime != null) {
            observeFilteredAnimals(zonedDateTime = zonedDateTime)
        } else {
            observeAllAnimals()
        }
    }

    private fun observeAllAnimals() {
        allAnimalsJob = viewModelScope.launch {
            if (::filteredAnimalsJob.isInitialized) {
                filteredAnimalsJob.cancelAndJoin()
            }
            MongoDB.getAllAnimals().collect { result ->
                animals.value = result
            }
        }
    }

    private fun observeFilteredAnimals(zonedDateTime: ZonedDateTime) {
        filteredAnimalsJob = viewModelScope.launch {
            if (::allAnimalsJob.isInitialized) {
                allAnimalsJob.cancelAndJoin()
            }
            MongoDB.getFilteredAnimals(zonedDateTime = zonedDateTime).collect { result ->
                animals.value = result
            }
        }
    }
}