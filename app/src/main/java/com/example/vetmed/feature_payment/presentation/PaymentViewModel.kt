package com.example.vetmed.feature_payment.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.presentation.util.Constants
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId


class PaymentViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var selectedVetUserId: MutableState<String> = mutableStateOf("")

    init {
        getVetIdArguments()
    }

    fun addVetTickets(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            val result = MongoDB.updateUser(
                id = savedStateHandle.get<String>(key = Constants.PAYMENT_SCREEN_ARGUMENT_KEY)!!
            )

            if (result is RequestState.Success) {
                // show some message saying the vet has been successfully added to your list or something like that
                onSuccess()
            } else if (result is RequestState.Error) {
                // show the error that the vet cannot be added with the error message as well
                onFailure(Exception(result.error.message))
            }
        }
    }

    private fun getVetIdArguments() {
        val key = savedStateHandle.get<String>(
            key = Constants.PAYMENT_SCREEN_ARGUMENT_KEY
        )!!
        Log.d("Get Vet Id", "getVetIdArguments:$key ")
        selectedVetUserId.value = key

    }
}