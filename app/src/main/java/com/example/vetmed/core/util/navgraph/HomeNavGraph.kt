package com.example.vetmed.core.util.navgraph


import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Button
import com.example.vetmed.core.util.BottomBarScreen
import com.example.vetmed.core.util.Screen
import com.example.vetmed.feature_animal.presentation.AnimalBase
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_appointment.AppointmentScreen
import com.example.vetmed.feature_appointment.AppointmentViewModel
import com.example.vetmed.feature_authentication.data.User
import com.example.vetmed.feature_authentication.presentation.util.Constants.KHALTI_TEST_KEY
import com.example.vetmed.feature_home.presentation.home.HomeScreen
import com.example.vetmed.feature_home.presentation.home.HomeViewModel
import com.example.vetmed.feature_payment.presentation.PaymentScreen
import com.example.vetmed.feature_payment.presentation.PaymentViewModel
import com.example.vetmed.feature_profile.presentation.ProfileBase
import com.example.vetmed.feature_vet.presentation.VetBase
import com.example.vetmed.feature_video_call.presentation.GetUserViewModel
import com.khalti.checkout.helper.Config
import com.khalti.checkout.helper.KhaltiCheckOut
import com.khalti.checkout.helper.OnCheckOutListener
import com.khalti.checkout.helper.PaymentPreference
import com.stevdzasan.messagebar.rememberMessageBarState

@Composable
fun HomeNavGraph(navController: NavHostController, navigateToLogIn: () -> Unit) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        home(
            navigateToPaymentScreenWithArgs = { vetId ->
                navController.navigate(Screen.Payment.passVetId(vetId))
            },
            onCallButtonClick = { userId ->
                Log.d("UserId", "HomeNavGraph: $userId")
                navController.navigate(Screen.Video.passUserId(userId))
            }
        )
        vetBase()
        animalBase()
        profileBase(navigateToLogIn = navigateToLogIn)
        paymentScreen(onBackPressed = {
            navController.popBackStack()
        })
        homeBase(
            onBackPressed = {
                navController.popBackStack()
            }
        )

    }
}

fun NavGraphBuilder.home(
    navigateToPaymentScreenWithArgs: (String) -> Unit,
    onCallButtonClick: (String) -> Unit
) {
    composable(route = BottomBarScreen.Home.route) {
        val homeViewModel: HomeViewModel = viewModel()
        val appointmentViewModel: AppointmentViewModel = viewModel()
        val isVet = homeViewModel.isVet
        val vets by homeViewModel.vets
        val users by appointmentViewModel.users
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isVet.value) {
                AppointmentScreen(
                    users = users,
                    onCallButtonClick = onCallButtonClick
                )
            } else {
                HomeScreen(onCallButtonClick = navigateToPaymentScreenWithArgs, vets = vets)
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun NavGraphBuilder.homeBase(
    onBackPressed: () -> Unit
) {
    composable(route = Screen.Video.route) {
        val getUserViewModel: GetUserViewModel = viewModel()
        val navController: NavHostController = rememberNavController()
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .navigationBarsPadding(),
        ) {
            VideoNavGraph(navController = navController, onBackPressed = onBackPressed, userId = getUserViewModel.selectedUserId.value)
        }
    }
}

fun NavGraphBuilder.paymentScreen(
    onBackPressed: () -> Unit
) {
    composable(route = Screen.Payment.route) {
        val context = LocalContext.current
        val paymentViewModel: PaymentViewModel = viewModel()
        val messageBarState = rememberMessageBarState()
        PaymentScreen(
            messageBarState = messageBarState,
            onBackPressed = onBackPressed,
            onPayButtonClick = {

                val config = Config.Builder(KHALTI_TEST_KEY, "Product ID", "Main", 11000L, object :
                    OnCheckOutListener {
                    override fun onError(action: String, errorMap: Map<String, String>) {
                        Log.i(action, errorMap.toString())
                        Log.d("PaymentError", "onError: $errorMap")
                        Toast.makeText(context, "Payment Was Not Successful", Toast.LENGTH_SHORT)
                            .show()

                    }

                    override fun onSuccess(data: Map<String, Any>) {
                        Log.i("success", data.toString())
                        // add the vet into the vet screen where the vets whose tickets you have bought will be added
                        Toast.makeText(context, "Payment Successful", Toast.LENGTH_SHORT).show()
                        paymentViewModel.addVetTickets(
                            onSuccess = {
                                messageBarState.addSuccess("Successfully added vet into the paid vet list")
                            },
                            onFailure = { exception ->
                                messageBarState.addError(exception)
                            }
                        )
                    }
                })
                    .paymentPreferences(
                        arrayListOf(
                            PaymentPreference.KHALTI,
                            PaymentPreference.EBANKING,
                            PaymentPreference.MOBILE_BANKING,
                            PaymentPreference.CONNECT_IPS,
                            PaymentPreference.SCT
                        )
                    )
                    .mobile("").build()

                val khaltiCheckOut = KhaltiCheckOut(context, config)
                khaltiCheckOut.show()
            })
        Button(onClick = {
            paymentViewModel.addVetTickets(onSuccess = {}, onFailure = {})
        }) {

        }

    }
}


fun NavGraphBuilder.vetBase() {
    composable(route = BottomBarScreen.Vet.route) {
        VetBase()
    }
}


fun NavGraphBuilder.animalBase(
) {
    composable(route = BottomBarScreen.Animal.route) {
        AnimalBase()
    }
}


fun NavGraphBuilder.profileBase(
    navigateToLogIn: () -> Unit
) {
    composable(route = BottomBarScreen.Profile.route) {
        ProfileBase(
            navigateToLogIn = navigateToLogIn
        )
    }
}