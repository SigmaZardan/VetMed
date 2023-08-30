package com.example.vetmed.feature_authentication.presentation.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.presentation.util.Constants
import com.example.vetmed.feature_authentication.presentation.util.Constants.APP_ID
import com.example.vetmed.feature_profile.domain.model.User
import com.google.gson.Gson
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.ext.profileAsBsonDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInViewModel : ViewModel() {
    var authenticated = mutableStateOf(false)
        private set
    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit,

        ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        Credentials.jwt(tokenId)
                    ).loggedIn
                }
                withContext(Dispatchers.Main) {
                    if (result) {
                        onSuccess()
                        delay(600)
                        insertUser()
                        authenticated.value = true
                    }


                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }


    suspend fun insertUser() {
        val currentUser = App.create(APP_ID).currentUser
        val userProfileJsonString = currentUser?.profileAsBsonDocument()?.toJson()
        val gson = Gson()
        val userInfo = gson.fromJson(userProfileJsonString, User::class.java)

        val result =
            MongoDB.insertUser(com.example.vetmed.feature_authentication.data.User().apply {
                userName = userInfo.name
                email = userInfo.email
                profile = userInfo.picture
            })

        if (result is RequestState.Success) {
            withContext(Dispatchers.Main) {
                Log.d("InsertUserSuccess", "insertUser:Success ")
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                Log.d("InsertUserError", "${result.error.message} ")
            }
        }
    }
}