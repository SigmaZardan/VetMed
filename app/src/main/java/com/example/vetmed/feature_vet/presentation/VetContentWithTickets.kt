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
import com.example.vetmed.feature_home.presentation.home.EmptyUserPage
import com.example.vetmed.feature_home.presentation.home.components.VetHolder
import org.mongodb.kbson.BsonObjectId

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
            items(items = userList) {
                VetHolderWithTicket(
                    vetUser = it,
                    onCallButtonClick = onCallButtonClick
                )
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

val userList = mutableListOf(
    User().apply {
        _id = org.mongodb.kbson.ObjectId("64fbbc7b4f973154d3781068")
        userName = "Alice"
        email = "alice@example.com"
        profile = ""
        description = "I'm a pet lover and a veterinarian."
    },
    User().apply {
        userName = "Bob"
        email = "bob@example.com"
        profile = ""
        description = "I'm a pet owner and a dog trainer."
        isVet = false
        isAvailable = true
    },
    User().apply {
        userName = "Cindy"
        email = "cindy@example.com"
        profile = ""
        description = "I'm a pet owner and a cat lover."
    },
    User().apply {
        userName = "Dan"
        email = "dan@example.com"
        profile = ""
        description = "I'm a pet owner and a bird watcher."
    }
)

