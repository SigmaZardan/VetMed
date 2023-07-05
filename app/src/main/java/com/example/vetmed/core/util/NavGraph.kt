package com.example.vetmed.core.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.feature_authentication.presentation.login.LoginScreen
import com.example.vetmed.feature_authentication.presentation.login.LogInViewModel
import com.example.vetmed.feature_authentication.presentation.util.Constants.APP_ID
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {

        loginRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        signupRoute()
        homeRoute()
    }
}

fun NavGraphBuilder.loginRoute(
    navigateToHome: () -> Unit
) {
    composable(route = Screen.Login.route) {
        val oneTapSignInState = rememberOneTapSignInState()
        val messageState = rememberMessageBarState()
        val loginViewModel: LogInViewModel = viewModel()
        val loadingState by loginViewModel.loadingState
        val authenticated by loginViewModel.authenticated

        LoginScreen(
            authenticated = authenticated,
            oneTapState = oneTapSignInState,
            messageState = messageState,
            googleButtonLoadingState = loadingState,
            onSignInButtonClick = {
                oneTapSignInState.open()
                loginViewModel.setLoading(true)
            },
            onTokenIdReceived = { tokenId ->
                loginViewModel.signInWithGoogle(
                    tokenId = tokenId,
                    onSuccess = {
                        messageState.addSuccess("Successfully Authenticated!")
                        loginViewModel.setLoading(false)
                    },
                    onError = { exception ->
                        messageState.addError(exception)
                        loginViewModel.setLoading(false)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageState.addError(Exception(message))
                loginViewModel.setLoading(false)
            },
            navigateToHome = navigateToHome
        )
    }
}

fun NavGraphBuilder.signupRoute() {
    composable(route = Screen.SignUp.route) {

    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    val user = App.create(APP_ID).currentUser
                    scope.launch(Dispatchers.IO) {
                        user?.logOut()
                    }

                }
            ) {
                Text(text = "LogOut")
            }


        }
    }
}
