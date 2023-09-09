package com.example.vetmed.feature_video_call.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vetmed.feature_authentication.presentation.util.Constants

class GetUserViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var selectedUserId: MutableState<String> = mutableStateOf("")

    init {
        getVetIdArguments()
    }

    private fun getVetIdArguments() {
        val key = savedStateHandle.get<String>(
            key = Constants.PAYMENT_SCREEN_ARGUMENT_KEY
        )!!
        Log.d("Get Vet Id", "getVetIdArguments:$key ")
        selectedUserId.value = key

    }
}