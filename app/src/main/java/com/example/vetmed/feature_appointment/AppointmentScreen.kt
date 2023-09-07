package com.example.vetmed.feature_appointment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vetmed.R
import com.example.vetmed.feature_animal.data.repository.Users
import com.example.vetmed.feature_animal.presentation.components.EmptyPage
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_appointment.components.AppointmentContent
import com.example.vetmed.feature_authentication.data.User
import com.example.vetmed.feature_home.presentation.home.components.VetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    onCallButtonClick: (String) -> Unit,
    users: Users
) {
    Scaffold(
        topBar = { VetTopBar() },
        content = {
            when (users) {
                is RequestState.Success -> {
                    AppointmentContent(
                        paddingValues = it,
                        users = users.data,
                        onCallButtonClick = onCallButtonClick
                    )
                }

                is RequestState.Error -> {
                    EmptyPage(
                        title = "Error",
                        subtitle = "${users.error.message}"
                    )
                }

                is RequestState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    EmptyPage(title = "No Vets Available", subtitle = "Try Logging In Later")

                }
            }


        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetTopBar(
) {

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icons8_veterinarian_50),
                    contentDescription = "Logo",
                    tint = Color(0xFF26A69A)
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text("My Appointments")
            }
        },
    )
}

@Composable
fun EmptyUserPage(
    title: String = "No Vets Found",
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )
    }

}

val appointments = mutableListOf(
    User().apply {
        userName = "Sanzeena"
        email = "sanzeena@gmail.com"
        profile = ""
        description = "I'm a pet lover and a veterinarian."
    },
    User().apply {
        userName = "Bibek"
        email = "bibekbhujel077@gmail.com"
        profile = ""
        description = "I'm a pet owner and a dog trainer."
    },

    )
