package com.example.vetmed.core.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "HOME",
        icon = Icons.Default.Home
    )

    object Vet : BottomBarScreen(
        route = "VET",
        icon = Icons.Default.HowToReg
    )


    object Animal : BottomBarScreen(
        route = "ANIMAL",
        icon = Icons.Default.Pets
    )

    object Profile : BottomBarScreen(
        route = "PROFILE",
        icon = Icons.Default.Person
    )
}


