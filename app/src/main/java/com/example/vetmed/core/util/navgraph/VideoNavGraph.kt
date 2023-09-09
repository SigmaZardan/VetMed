package com.example.vetmed.core.util.navgraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.vetmed.feature_video_call.presentation.RoomScreen
import com.example.vetmed.feature_video_call.presentation.VideoScreen


@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun VideoNavGraph(
    navController: NavHostController,
    onBackPressed: () -> Unit,
    userId: String
) {
    NavHost(
        navController = navController,
        startDestination = "room_screen"
    ) {
        composable(route = "room_screen") {
            RoomScreen(
                onNavigate = navController::navigate,
                onBackPressed = onBackPressed,
                userId = userId
            )
        }
        composable(
            route = "video_screen/{roomName}",
            arguments = listOf(
                navArgument(name = "roomName") {
                    type = NavType.StringType
                }
            )
        ) {
            val roomName = it.arguments?.getString("roomName") ?: return@composable
            VideoScreen(
                roomName = roomName,
                onNavigateUp = {
                    navController.navigateUp() }
            )
        }
    }
}

