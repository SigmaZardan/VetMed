package com.example.vetmed.feature_vet.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.vetmed.core.util.navgraph.VetNavGraph


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun VetBase(
) {
    val navController: NavHostController = rememberNavController()
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface).statusBarsPadding().navigationBarsPadding(),

    ) {
        VetNavGraph(navController = navController)

    }
}