package com.example.vetmed.feature_vet.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.data.repository.Users
import com.example.vetmed.feature_animal.data.repository.VetUsers
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId

class VetViewModel : ViewModel() {
    private var vets: MutableState<VetUsers> = mutableStateOf(RequestState.Idle)
    var listOfVets = mutableListOf<User>()
    var vetsToPass: MutableState<Users> = mutableStateOf(RequestState.Idle)

    init {
        getAllVetTickets()
    }

    private fun getAllVetTickets() {
        viewModelScope.launch {
            MongoDB.getAllVetsWithTickets().collect { result ->
                vets.value = result
            }

            if (vets.value is RequestState.Success) {
                val vetIds = (vets.value as RequestState.Success<List<String>>).data
                vetIds.forEach { id ->
                    val vetResult = MongoDB.getVetWithGivenId(BsonObjectId.Companion.invoke(id))
                    if (vetResult is RequestState.Success) {
                        listOfVets.add(vetResult.data)
                    }
                }
            } else if (vets.value is RequestState.Error) {
                Log.d("VetsWithTickets", "${(vets.value as RequestState.Error).error.message} ")
            }
        }

        vetsToPass.value = RequestState.Success(listOfVets)


    }

}