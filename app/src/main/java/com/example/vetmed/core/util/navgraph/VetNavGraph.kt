package com.example.vetmed.core.util.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.core.util.Screen
import com.example.vetmed.feature_vet.presentation.VetScreen

@Composable
fun VetNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Vet.route
    ) {
        vetRoute()
    }
}

fun NavGraphBuilder.vetRoute() {
    composable(route = Screen.Vet.route) {
        VetScreen(onCallButtonClick = {})
    }
}