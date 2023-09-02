package com.example.vetmed.feature_vet.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User
import com.example.vetmed.feature_home.presentation.home.components.EmptyUserPage
import com.example.vetmed.feature_home.presentation.home.components.VetHolder

@Composable
fun VetContentWithTickets(
    paddingValues: PaddingValues,
    vets: RequestState<List<User>>,
    onCallButtonClick: (String) -> Unit
) {

    if (vets is RequestState.Success) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .padding(paddingValues)
                .navigationBarsPadding()
                .padding(bottom = 35.dp)
        ) {
            items(items = vets.data) {
                VetHolderWithTicket(
                    vetUser = it,
                    onCallButtonClick = onCallButtonClick)
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }

    } else if (vets is RequestState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onBackground
            )
        }

    } else if (vets is RequestState.Error) {
        Log.d("Vets", "VetContentWithTickets: ${vets.error.message}")
    } else {
        EmptyUserPage()
    }

}
