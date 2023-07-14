package com.example.vetmed.feature_home.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_authentication.presentation.util.Constants
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "HomeViewModel"
class HomeViewModel : ViewModel() {
    var loggedOut = mutableStateOf(false)
        private set


    fun logOut(

    ) {
        viewModelScope.launch {
            try {
                val user = App.create(Constants.APP_ID).currentUser
                withContext(Dispatchers.IO) {
                    user?.logOut()
                    loggedOut.value = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "Unable to LogOut ")
                }
            }
        }
    }
}
