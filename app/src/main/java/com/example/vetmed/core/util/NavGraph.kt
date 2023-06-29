package com.example.vetmed.core.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.feature_authentication.presentation.login.LoginScreen
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

        LoginScreen(
            oneTapState = oneTapSignInState,
            googleButtonLoadingState = oneTapSignInState.opened
        ) {
            oneTapSignInState.open()
        }
    }
}

fun NavGraphBuilder.signupRoute() {
    composable(route = Screen.SignUp.route) {

    }
}
