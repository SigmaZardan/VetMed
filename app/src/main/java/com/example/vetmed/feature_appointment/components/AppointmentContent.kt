package com.example.vetmed.feature_appointment.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vetmed.feature_authentication.data.User
import com.example.vetmed.feature_home.presentation.home.components.VetHolder

@Composable
fun AppointmentContent(
    paddingValues: PaddingValues,
    users: List<User>,
    onCallButtonClick: (String) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(paddingValues)
            .navigationBarsPadding()
            .padding(bottom = 35.dp)
    ) {
        items(items = users) {
            AppointmentHolder(
                user = it,
                onCallButtonClick = onCallButtonClick
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }


}
