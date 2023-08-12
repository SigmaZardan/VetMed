package com.example.vetmed.feature_profile.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_profile.domain.model.User
import com.google.gson.Gson
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.ext.profileAsBsonDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "ProfileViewModel"
class ProfileViewModel : ViewModel() {
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


    fun getUserData(): User {
        var user = User()
        viewModelScope.launch {
            try {
                val currentUser = App.create(Constants.APP_ID).currentUser
                val userProfileJsonString = currentUser?.profileAsBsonDocument()?.toJson()
                val gson = Gson()
                 user = gson.fromJson(userProfileJsonString, User::class.java)
            }
            catch(e : Exception) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "Cannot get the user data" )
                }
            }
        }
        return user
    }
}