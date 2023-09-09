package com.example.vetmed.core.util.navgraph

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
fun UserVideoNavGraph(
    navController: NavHostController,
    roomName: String
) {
    NavHost(
        navController = navController,
        startDestination = "user_video_screen"
    ) {

        composable(
            route = "user_video_screen",
        ) {
            VideoScreen(
                roomName = roomName,
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}