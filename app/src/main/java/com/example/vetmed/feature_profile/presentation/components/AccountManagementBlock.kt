package com.example.vetmed.feature_profile.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vetmed.core.util.ProfileOptions


@Composable
fun AccountManagementBlock(
    navController: NavHostController,
) {
    val options = listOf(
        ProfileOptions.Share,
        ProfileOptions.AboutUs,
        ProfileOptions.PrivacyPolicy,
    )
    LazyColumn(
        modifier = Modifier.height(120.dp)
    ) {
        items(options) { option ->
            OptionRowItem(
                iconId = option.iconId,
                option = option.route,
                onOptionClicked = {
                    navController.navigate(route = option.route)
                }
            )
        }


    }
}

