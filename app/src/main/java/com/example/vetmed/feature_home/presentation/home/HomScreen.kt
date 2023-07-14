package com.example.vetmed.feature_home.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    signedOut: Boolean,
    onLogOutButtonClick: () -> Unit,
    navigateToLogIn: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onLogOutButtonClick
        ) {
            Text(text = "SignOut")
        }

    }

    LaunchedEffect(signedOut) {
        if (signedOut) {
            navigateToLogIn()
        }
    }
}
