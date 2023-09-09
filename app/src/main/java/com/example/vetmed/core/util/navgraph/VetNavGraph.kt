package com.example.vetmed.core.util.navgraph

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vetmed.core.util.Screen
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.presentation.components.EmptyPage
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.presentation.util.Constants
import com.example.vetmed.feature_vet.presentation.VetScreen
import com.example.vetmed.feature_video_call.presentation.GetRoomViewModel
import com.example.vetmed.feature_video_call.presentation.GetUserViewModel
import kotlinx.coroutines.launch

@Composable
fun VetNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Vet.route
    ) {
        vetRoute(
            navigateToUserVideoScreenWithArguments = { roomName ->
                navController.navigate(route = Screen.UserVideoCallScreen.passRoomName(roomName = roomName))
            }
        )
        videoUser(
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
}

fun NavGraphBuilder.vetRoute(
    navigateToUserVideoScreenWithArguments: (String) -> Unit
) {
    composable(route = Screen.Vet.route) {
        val scope = rememberCoroutineScope()
        VetScreen(onCallButtonClick = { userId ->
            scope.launch {
                val result = MongoDB.getRoom(userId)
                if (result is RequestState.Success) {
                    Log.d("RoomValue", "vetRoute:${result.data} ")
                    navigateToUserVideoScreenWithArguments(result.data)
                }
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun NavGraphBuilder.videoUser(
    navigateBack: () -> Unit
) {
    composable(route = Screen.UserVideoCallScreen.route) {
        val getRoomViewModel: GetRoomViewModel = viewModel()
        val roomName = getRoomViewModel.selectedRoom
        val navController: NavHostController = rememberNavController()
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(bottom = 50.dp),
        ) {

            if (roomName.value.isNotEmpty()) {
                UserVideoNavGraph(navController = navController, roomName = roomName.value)
            } else {
                NoRoom(
                    navigateBack = navigateBack
                )
            }


        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoRoom(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "NavigateBack"
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("NO ROOM CREATED BY VET")
                }

            })
        },
        content = {
            EmptyPage(
                title = "NO ROOM CREATED BY VET",
                subtitle = "Wait for a minute"
            )
        }
    )
}