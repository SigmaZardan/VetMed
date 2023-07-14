package com.example.vetmed.core.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.feature_authentication.presentation.login.LoginScreen
import com.example.vetmed.feature_authentication.presentation.login.LogInViewModel
import com.example.vetmed.feature_home.presentation.home.HomeScreen
import com.example.vetmed.feature_home.presentation.home.HomeViewModel
import com.example.vetmed.feature_home.presentation.home.components.DisplayAlertDialog
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
        loginRoute(
            navigateToHome = {
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        )
        signupRoute()
        homeRoute(
            navigateToLogIn = {
                navController.popBackStack()
                navController.navigate(Screen.Login.route)
            }
        )
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
            navigateToHome = navigateToHome,
            onGoogleAccountAdditionSuccess = {
                messageState.addSuccess("Google Account Added Successfully!")
            },
            onGoogleAccountAdditionUnSuccess = {
                messageState.addError(Exception("Unable to add Google Account!"))
            },
        )
    }
}

fun NavGraphBuilder.signupRoute() {
    composable(route = Screen.SignUp.route) {

    }
}

fun NavGraphBuilder.homeRoute(
    navigateToLogIn: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val homeViewModel: HomeViewModel = viewModel()
        val signedOut by homeViewModel.loggedOut
        var signOutDialogOpened by remember { mutableStateOf(false) }
        HomeScreen(
            signedOut = signedOut,
            onLogOutButtonClick = {
                signOutDialogOpened = true
            },
            navigateToLogIn = navigateToLogIn
        )

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onYesClicked = {
                homeViewModel.logOut()
            },
            onDialogClosed = {
            }
        )
    }
}

