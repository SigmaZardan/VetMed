package com.example.vetmed.feature_profile.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vetmed.feature_profile.presentation.components.AccountManagementBlock
import com.example.vetmed.feature_profile.presentation.components.ProfileAndNameSection
import com.example.vetmed.ui.theme.VetMedTheme


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profilePicUrl: String,
    userName: String,
    email: String,
    signedOut: Boolean,
    onLogOutClick: () -> Unit,
    navigateToLogIn: () -> Unit
) {


    Scaffold(
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProfileAndNameSection(
                profilePicUrl = profilePicUrl,
                userName = userName,
                email = email
            )
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            AccountManagementBlock(navController = navController)


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onLogOutClick()
                    }
                    .padding(bottom = 10.dp),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "LogOut",
                        style = MaterialTheme.typography.h6
                    )
                }
            }
            LaunchedEffect(signedOut) {
                if (signedOut) {
                    navigateToLogIn()
                }
            }

        }
    }

}


@Preview
@Composable
fun ProfileScreenPreview() {
    VetMedTheme {
        ProfileScreen(
            navController = rememberNavController(),
            profilePicUrl = "anything",
            userName = "Bibek",
            email = "bibekbhujel077@gmail.com",
            signedOut = false,
            onLogOutClick = {},
            navigateToLogIn = {}
        )
    }
}
