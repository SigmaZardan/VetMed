package com.example.vetmed.core.util

sealed class Screen(val route: String) {
    object Login : Screen(route = "login_screen")
    object SignUp : Screen(route = "signup_screen")
    object Home : Screen(route = "home_screen")
}
