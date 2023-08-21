package com.example.vetmed.core.util

import com.example.vetmed.feature_authentication.presentation.util.Constants.ADD_ANIMAL_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Login : Screen(route = "login_screen")
    object SignUp : Screen(route = "signup_screen")
    object Home : Screen(route = "home_screen")
    object Profile : Screen(route = "profile_screen")
    object Animal : Screen(route = "animal_screen")
    object Vet : Screen(route = "vet_screen")
    object AddAnimal : Screen(
        route = "add_animal_screen?$ADD_ANIMAL_SCREEN_ARGUMENT_KEY=" +
                "{$ADD_ANIMAL_SCREEN_ARGUMENT_KEY}"
    ) {
        fun passAnimalId(animalId: String) =
            "add_animal_screen?$ADD_ANIMAL_SCREEN_ARGUMENT_KEY=$animalId"
    }
}
