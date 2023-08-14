package com.example.vetmed.core.util.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.core.util.Screen
import com.example.vetmed.feature_animal.presentation.AddAnimal
import com.example.vetmed.feature_animal.presentation.AnimalScreen


@Composable
fun AnimalNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Animal.route
    ) {
        animalRoute(navigateToAddAnimalScreen = {
            navController.popBackStack()
            navController.navigate(Screen.AddAnimal.route)
        })
        addAnimalRoute()
    }
}

fun NavGraphBuilder.animalRoute(
    navigateToAddAnimalScreen: () -> Unit
) {
    composable(route = Screen.Animal.route) {
        AnimalScreen(
            navigateToAddAnimalScreen = navigateToAddAnimalScreen
        )
    }
}


fun NavGraphBuilder.addAnimalRoute(

) {
    composable(route = Screen.AddAnimal.route) {
        AddAnimal()
    }
}
