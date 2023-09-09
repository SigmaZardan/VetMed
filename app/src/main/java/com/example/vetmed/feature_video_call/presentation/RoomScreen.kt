package com.example.vetmed.feature_video_call.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import okhttp3.internal.wait

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    onNavigate: (String) -> Unit,
    viewModel: RoomViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBackPressed: () -> Unit,
    userId: String
) {
    viewModel.userId.value = userId
    LaunchedEffect(key1 = true) {
        viewModel.onJoinEvent.collectLatest { name ->
            Log.d("RoomName", "RoomScreen: $name")
            onNavigate("video_screen/$name")
        }
    }

    Scaffold(
        topBar = {
            RoomScreenTopBar(
                onBackPressed = onBackPressed
            )
        },
        content = {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                TextField(
                    value = viewModel.roomName.value.text,
                    onValueChange = viewModel::onRoomEnter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    isError = viewModel.roomName.value.error != null,
                    placeholder = {
                        Text(text = "Enter a room name")
                    },
                )
                viewModel.roomName.value.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            viewModel.onJoinRoom()
                        },
                    shape = Shapes().extraSmall,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = "JOIN",
                            tint = Color.Unspecified,
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CREATE",
                            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                        )
                    }
                }
            }

        }
    )

}

@Preview
@Composable
fun RoomScreenPreview() {
    RoomScreen(onNavigate = {}, onBackPressed = {}, userId = "")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreenTopBar(
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate Back Button"
                )
            }
        },
        title = {
            Text(
                text = "VIDEO CALL ROOM",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        })
}
