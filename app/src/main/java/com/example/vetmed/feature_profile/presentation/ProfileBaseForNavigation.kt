package com.example.vetmed.feature_profile.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vetmed.core.util.ProfileNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun ProfileBase(
    navigateToLogIn: () -> Unit
) {
    Scaffold(

    ) {
        val navController: NavHostController = rememberNavController()
        ProfileNavGraph(navController, navigateToLogIn = navigateToLogIn)
    }
}