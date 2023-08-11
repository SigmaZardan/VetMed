package com.example.vetmed.feature_home.presentation.home

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vetmed.core.util.navgraph.HomeNavGraph
import com.example.vetmed.feature_home.presentation.home.components.BottomBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBase(
    navigateToLogIn: () -> Unit
) {
    val navController: NavHostController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) },

    ) {
        HomeNavGraph(navController = navController,navigateToLogIn = navigateToLogIn)
    }
}
