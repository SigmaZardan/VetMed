package com.example.vetmed.core.util.navgraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.wear.compose.material.dialog.DialogDefaults
import com.example.vetmed.core.util.Screen
import com.example.vetmed.feature_animal.presentation.AddAnimal
import com.example.vetmed.feature_animal.presentation.AnimalScreen
import com.example.vetmed.feature_animal.presentation.AnimalViewModel


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
        val animalViewModel: AnimalViewModel = viewModel()
        val animals by animalViewModel.animals
        AnimalScreen(
            animals = animals,
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
