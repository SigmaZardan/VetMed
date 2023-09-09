package com.example.vetmed.core.util.navgraph

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.core.util.ProfileOptions
import com.example.vetmed.core.util.Screen
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
        aboutUs(
            navigateBack = {
                navController.popBackStack()
            }
        )
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
        val user = profileViewModel.getUserData()

        ProfileScreen(
            navController = navController,
            profilePicUrl = "${user.picture}",
            userName = "${user.name}",
            email = "${user.email}",
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
    composable(route = ProfileOptions.Share.route) {
        val context = LocalContext.current
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
        context.startActivity(Intent.createChooser(intent, "Share Using"))
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.aboutUs(
    navigateBack: () -> Unit
) {
    composable(route = ProfileOptions.AboutUs.route) {

        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .navigationBarsPadding(),
            topBar = {
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
                    androidx.compose.material3.Text("ABOUT US")
                }
            },
            content = {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 30.dp, bottom = 30.dp, start = 5.dp, end = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your privacy is important to us. This Privacy Policy outlines how VETMED collects, uses, and\n" +
                                "safeguards your personal information and data. By using our app, you consent to the practices\n" +
                                "described in this Privacy Policy.\n" +
                                "Information We Collect\n" +
                                "1. Personal Information: We may collect personal information that you provide directly to us,\n" +
                                "such as your name, email address, phone number, and payment information when you register\n" +
                                "for an account, schedule appointments, or use our services.\n" +
                                "2. Animal Information: We collect information about your animals, including their names,\n" +
                                "species, breeds, ages, medical histories, and photos, to facilitate veterinary consultations and\n" +
                                "services.\n" +
                                "3. Usage Data: We may collect information about how you use our app, including your\n" +
                                "interactions, preferences, and activities. This data helps us improve our app's functionality and\n" +
                                "user experience.\n" +
                                "How We Use Your Information\n" +
                                "1. Providing Services: We use the information you provide to offer virtual veterinary\n" +
                                "consultations, manage appointments, and facilitate communication between you and licensed\n" +
                                "veterinarians.\n" +
                                "2. Improvement: Your usage data helps us analyze app performance, identify areas for\n" +
                                "improvement, and develop new features.\n" +
                                "3. Communication: We may use your contact information to send you appointment reminders,\n" +
                                "notifications, and updates related to our services.\n" +
                                "4. Payment Processing: Your payment information is securely processed for billing purposes\n" +
                                "when you book appointments and pay for services.\n" +
                                "Data Security\n" +
                                "We take data security seriously and employ industry-standard measures to protect your\n" +
                                "personal information. However, no online platform is entirely secure, and we cannot guarantee\n" +
                                "the absolute security of your data.\n" +
                                "Sharing of Information\n" +
                                "We do not sell or rent your personal information to third parties. We may share your information\n" +
                                "with licensed veterinarians to facilitate consultations and provide appropriate care for your\n" +
                                "animals.\n" +
                                "Legal Compliance\n" +
                                "We may disclose your information as required by law, legal process, or government requests,\n" +
                                "and to protect our rights, privacy, safety, or property, or that of the public.\n" +
                                "Your Choices\n" +
                                "You can access and update your personal information in your account settings. You may also\n" +
                                "request the deletion of your account, although certain information may be retained for legal and\n" +
                                "business purposes.\n" +
                                "Changes to this Policy\n" +
                                "We may update this Privacy Policy from time to time. Any changes will be posted on this page,\n" +
                                "and the \"Last Updated\" date will reflect the revised policy.\n" +
                                "Contact Us\n" +
                                "If you have any questions, concerns, or requests related to your personal information or this\n" +
                                "Privacy Policy, please contact us at vetmed@gmail.com].\n" +
                                "Please ensure you carefully review and adapt the privacy policy to suit your app's specific\n" +
                                "features, legal requirements, and company practices. Always consult with legal professionals to\n" +
                                "ensure compliance with relevant laws and regulations.",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
            }
        )
    }
}

fun NavGraphBuilder.privacyPolicy() {
    composable(route = ProfileOptions.PrivacyPolicy.route) {
        Text("What is the privacy policy here my boy ???? can you give me some examples here and there")
    }
}

