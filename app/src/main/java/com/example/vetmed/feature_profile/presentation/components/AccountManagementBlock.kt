package com.example.vetmed.feature_profile.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.vetmed.core.util.ProfileOptions


@Composable
fun AccountManagementBlock(
    navController: NavHostController
) {
    val options = listOf(
        ProfileOptions.Share,
        ProfileOptions.AboutUs,
        ProfileOptions.PrivacyPolicy,
    )
    LazyColumn {
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

