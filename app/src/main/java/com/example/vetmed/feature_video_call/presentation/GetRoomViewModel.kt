package com.example.vetmed.feature_video_call.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vetmed.feature_authentication.presentation.util.Constants

class GetRoomViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var selectedRoom: MutableState<String> = mutableStateOf("")

    init {
        getVetIdArguments()
    }

    private fun getVetIdArguments() {
        val key = savedStateHandle.get<String>(
            key = Constants.USER_VIDEO_SCREEN_ARGUMENT_KEY
        )!!
        Log.d("Get Vet Id", "getVetIdArguments:$key ")
        selectedRoom.value = key

    }
}