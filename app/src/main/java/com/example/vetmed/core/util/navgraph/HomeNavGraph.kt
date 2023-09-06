package com.example.vetmed.core.util.navgraph


import android.util.Log
import android.widget.Toast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.wear.compose.material.Button
import com.example.vetmed.core.util.BottomBarScreen
import com.example.vetmed.core.util.Screen
import com.example.vetmed.feature_animal.presentation.AnimalBase
import com.example.vetmed.feature_authentication.presentation.util.Constants.TEST_KEY
import com.example.vetmed.feature_home.presentation.home.HomeScreen
import com.example.vetmed.feature_home.presentation.home.HomeViewModel
import com.example.vetmed.feature_payment.presentation.PaymentScreen
import com.example.vetmed.feature_payment.presentation.PaymentViewModel
import com.example.vetmed.feature_profile.presentation.ProfileBase
import com.example.vetmed.feature_vet.presentation.VetBase
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
            }
        )
        vetBase()
        animalBase()
        profileBase(navigateToLogIn = navigateToLogIn)
        paymentScreen(onBackPressed = {
            navController.popBackStack()
        })

    }
}

fun NavGraphBuilder.home(
    navigateToPaymentScreenWithArgs: (String) -> Unit
) {
    composable(route = BottomBarScreen.Home.route) {
        val homeViewModel: HomeViewModel = viewModel()
        val isVet = homeViewModel.isVet
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isVet.value) {
                Text("this is for the vets")
            }
            else {
                HomeScreen(onCallButtonClick = navigateToPaymentScreenWithArgs)
            }

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

                val config = Config.Builder(TEST_KEY, "Product ID", "Main", 11000L, object :
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
        Button(onClick = { paymentViewModel.addVetTickets(onSuccess = {}, onFailure = {}) }) {

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