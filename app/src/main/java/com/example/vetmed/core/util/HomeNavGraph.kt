package com.example.vetmed.core.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.feature_profile.presentation.ProfileBase
import com.example.vetmed.feature_profile.presentation.ProfileScreen

@Composable
fun HomeNavGraph(navController: NavHostController, navigateToLogIn: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        home()
        profileBase(navigateToLogIn = navigateToLogIn)

    }
}

fun NavGraphBuilder.home() {
    composable(route = BottomBarScreen.Home.route) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("This is the home screen where the list of veterinarians should be displayed")
        }
    }

}

fun NavGraphBuilder.profileBase(
    navigateToLogIn: () -> Unit
) {
    composable(route = BottomBarScreen.Profile.route) {
        ProfileBase(
            navigateToLogIn = navigateToLogIn
        )
    }
}