package com.example.vetmed.core.util

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.feature_profile.presentation.components.DisplayAlertDialog
import com.example.vetmed.feature_profile.presentation.ProfileScreen
import com.example.vetmed.feature_profile.presentation.ProfileViewModel

@Composable
fun ProfileNavGraph(
    navController: NavHostController,
    navigateToLogIn: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Profile.route
    ) {
        profileRoute(
            navController = navController,
            navigateToLogIn = navigateToLogIn
        )
        share()
        aboutUs()
        privacyPolicy()
    }
}

fun NavGraphBuilder.profileRoute(
    navController: NavHostController,
    navigateToLogIn: () -> Unit
) {
    composable(route = Screen.Profile.route) {
        val profileViewModel: ProfileViewModel = viewModel()
        val signedOut by profileViewModel.loggedOut
        var signOutDialogOpened by remember { mutableStateOf(false) }

        ProfileScreen(
            navController = navController,
            profilePicUrl = "asfsafasfa",
            userName = "Bibek bhujel",
            email = "bibekbhujel077@gmail.com",
            signedOut = signedOut,
            onLogOutClick = {
                signOutDialogOpened = true
            },
            navigateToLogIn = {
                navigateToLogIn()
            }
        )
        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are you sure want to Sign Out from your Google Account?",
            dialogOpened = signOutDialogOpened,
            onYesClicked = {
                profileViewModel.logOut()
            },
            onDialogClosed = {
                signOutDialogOpened = false
            }
        )
    }

}

fun NavGraphBuilder.share() {
    composable(route = Options.Share.route) {
        Text("share things for the wellfare of the society")
    }

}

fun NavGraphBuilder.aboutUs() {
    composable(route = Options.AboutUs.route) {
        Text("about us and  other things as well")
    }
}

fun NavGraphBuilder.privacyPolicy() {
    composable(route = Options.PrivacyPolicy.route) {
        Text("What is the privacy policy here my boy ???? can you give me some examples here and there")
    }
}

