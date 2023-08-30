package com.example.vetmed.core.util.navgraph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.core.util.BottomBarScreen
import com.example.vetmed.feature_animal.presentation.AnimalBase
import com.example.vetmed.feature_home.presentation.home.HomeScreen
import com.example.vetmed.feature_profile.presentation.ProfileBase
import com.example.vetmed.feature_vet.presentation.VetBase

@Composable
fun HomeNavGraph(navController: NavHostController, navigateToLogIn: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        home()
        vetBase()
        animalBase()
        profileBase(navigateToLogIn = navigateToLogIn)

    }
}

fun NavGraphBuilder.home() {
    composable(route = BottomBarScreen.Home.route) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
          HomeScreen()
        }
    }

}

fun NavGraphBuilder.vetBase() {
    composable(route = BottomBarScreen.Vet.route) {
        VetBase()
    }
}


fun NavGraphBuilder.animalBase(
) {
    composable(route = BottomBarScreen.Animal.route) {
        AnimalBase()
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