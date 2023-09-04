package com.example.vetmed.feature_vet.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.data.repository.VetUsers
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId

class VetViewModel : ViewModel() {
    var vets: MutableState<VetUsers> = mutableStateOf(RequestState.Idle)
    var vetIds: List<String> = mutableStateListOf()
    val listOfVets: MutableList<User> = mutableStateListOf()
    init {
        getAllVetTickets()
    }

    private fun getAllVetTickets() {
        viewModelScope.launch {
            try {
                val vetsResult =
                    MongoDB.getAllVetsWithTickets().first() // Wait for the first result

                if (vetsResult is RequestState.Success) {
                    vets.value = vetsResult
                    vetIds = vetsResult.data
                    Log.d("VetIds", "getAllVetTickets: $vetIds")

                    vetIds.forEach { id ->
                        try {
                            val userResult =
                                MongoDB.getVetWithGivenId(BsonObjectId.Companion.invoke(id)).first()
                            if (userResult is RequestState.Success) {
                                listOfVets.add(userResult.data)
                                Log.d("VetUser", "Getuser: ${userResult.data.email}")
                                Log.d("VetUserList", "getAllVetTickets:${listOfVets[0].email}")
                            } else if (userResult is RequestState.Error) {
                                // Handle the error if needed
                                Log.e("VetUser", "Error fetching user: ${userResult.error.message}")
                            }
                        } catch (e: Exception) {
                            // Handle exceptions during user retrieval
                            Log.e("VetUser", "Exception during user retrieval: $e")
                        }
                    }
                } else if (vetsResult is RequestState.Error) {
                    // Handle the error if needed
                    Log.e("VetIds", "Error fetching vet IDs: ${vetsResult.error.message}")
                }
            } catch (e: Exception) {
                // Handle exceptions during vet retrieval
                Log.e("VetIds", "Exception during vet retrieval: $e")
            }
        }
    }

}



