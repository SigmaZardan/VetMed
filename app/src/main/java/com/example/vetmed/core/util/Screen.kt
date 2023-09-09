package com.example.vetmed.core.util

import com.example.vetmed.feature_authentication.presentation.util.Constants.ADD_ANIMAL_SCREEN_ARGUMENT_KEY
import com.example.vetmed.feature_authentication.presentation.util.Constants.PAYMENT_SCREEN_ARGUMENT_KEY
import com.example.vetmed.feature_authentication.presentation.util.Constants.USER_VIDEO_SCREEN_ARGUMENT_KEY
import com.example.vetmed.feature_authentication.presentation.util.Constants.VIDEO_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Login : Screen(route = "login_screen")
    object SignUp : Screen(route = "signup_screen")
    object Home : Screen(route = "home_screen")
    object Profile : Screen(route = "profile_screen")
    object Animal : Screen(route = "animal_screen")
    object Vet : Screen(route = "vet_screen")
    object Video : Screen(
        route = "video_screen?$VIDEO_SCREEN_ARGUMENT_KEY=" +
                "{$VIDEO_SCREEN_ARGUMENT_KEY}"
    ) {
        fun passUserId(userId: String) = "video_screen?$VIDEO_SCREEN_ARGUMENT_KEY=$userId"
    }

    object Payment : Screen(
        route = "payment_screen?$PAYMENT_SCREEN_ARGUMENT_KEY=" +
                "{$PAYMENT_SCREEN_ARGUMENT_KEY}"
    ) {


        fun passVetId(vetId: String) =
            "payment_screen?$PAYMENT_SCREEN_ARGUMENT_KEY=$vetId"

    }

    object AddAnimal : Screen(
        route = "add_animal_screen?$ADD_ANIMAL_SCREEN_ARGUMENT_KEY=" +
                "{$ADD_ANIMAL_SCREEN_ARGUMENT_KEY}"
    ) {
        fun passAnimalId(animalId: String) =
            "add_animal_screen?$ADD_ANIMAL_SCREEN_ARGUMENT_KEY=$animalId"

    }
    object UserVideoCallScreen : Screen(
        route = "user_video_screen?$USER_VIDEO_SCREEN_ARGUMENT_KEY=" +
                "{$USER_VIDEO_SCREEN_ARGUMENT_KEY}"
    ) {
        fun passRoomName(roomName: String) =
            "user_video_screen?$USER_VIDEO_SCREEN_ARGUMENT_KEY=$roomName"

    }
}
