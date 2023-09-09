package com.example.vetmed.feature_video_call.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.presentation.util.Constants
import com.example.vetmed.feature_authentication.presentation.util.Constants.VIDEO_SCREEN_ARGUMENT_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class RoomViewModel(
) : ViewModel() {

    var userId: MutableState<String> = mutableStateOf("")
    private val _roomName = mutableStateOf(TextFieldState())
    val roomName: State<TextFieldState> = _roomName

    private val _onJoinEvent = MutableSharedFlow<String>()
    val onJoinEvent = _onJoinEvent.asSharedFlow()

    fun onRoomEnter(name: String) {
        _roomName.value = roomName.value.copy(
            text = name
        )
    }

    fun onJoinRoom() {
        if (roomName.value.text.isBlank()) {
            _roomName.value = roomName.value.copy(
                error = "The room can't be empty"
            )
            return
        }

        viewModelScope.launch {
            addRoom()
            _onJoinEvent.emit(roomName.value.text)
        }
    }

    private fun addRoom() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = MongoDB.addRoom(roomName.value.text, userId.value)
            if (result is RequestState.Success) {
                Log.d("RoomAdd", "addRoom: ${result.data}")
            }
        }

    }


}