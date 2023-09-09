package com.example.vetmed.feature_vet.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vetmed.R
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VetScreen(
    onCallButtonClick: (String) -> Unit
) {
    val vetViewModel: VetViewModel = viewModel()
    val listOfVets = vetViewModel.listOfVets

    Scaffold(
        topBar = { VetTopBar() },
        content = {
            VetContentWithTickets(
                paddingValues = it,
                vets = RequestState.Success(listOfVets),
                onCallButtonClick = onCallButtonClick
            )
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

                Text("Vets")
            }
        },
    )
}

