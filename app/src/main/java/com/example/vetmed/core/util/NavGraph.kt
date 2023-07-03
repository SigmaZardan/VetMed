package com.example.vetmed.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.feature_authentication.presentation.login.LoginScreen
import com.example.vetmed.feature_authentication.presentation.login.LogInViewModel
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState


@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        startDestination = startDestination,
        navController = navController
    ) {

        loginRoute()
        signupRoute()
    }
}

fun NavGraphBuilder.loginRoute(
) {
    composable(route = Screen.Login.route) {
        val oneTapSignInState = rememberOneTapSignInState()
        val messageState = rememberMessageBarState()
        val loginViewModel: LogInViewModel = viewModel()
        val loadingState by loginViewModel.loadingState

        LoginScreen(
            oneTapState = oneTapSignInState,
            messageState = messageState,
            googleButtonLoadingState = loadingState,
            onSignInButtonClick = {
                oneTapSignInState.open()
            },
            onTokenIdReceived = { tokenId ->
                loginViewModel.signInWithGoogle(
                    tokenId = tokenId,
                    onSuccess = { isSuccess ->
                        if (isSuccess) {
                            messageState.addSuccess("Successfully Authenticated!")
                            loginViewModel.setLoading(false)
                        }
                    },
                    onError = { exception ->
                        messageState.addError(exception)
                    }
                )
            },
            onDialogDismissed = { message ->
                messageState.addError(Exception(message))
            }
        )
    }
}

fun NavGraphBuilder.signupRoute() {
    composable(route = Screen.SignUp.route) {

    }
}
