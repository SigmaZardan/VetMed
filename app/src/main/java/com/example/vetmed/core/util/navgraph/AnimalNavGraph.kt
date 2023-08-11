package com.example.vetmed.core.util.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.core.util.Screen


@Composable
fun AnimalNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Animal.route
    ) {
        animalRoute()
    }
}

fun NavGraphBuilder.animalRoute() {
    composable(route = Screen.Animal.route) {
        Text("This is section for adding animal informations")
    }
}
