package com.example.vetmed.core.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.example.vetmed.R


sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "HOME",
        icon = Icons.Default.Home
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

