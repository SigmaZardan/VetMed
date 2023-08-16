package com.example.vetmed.feature_animal.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.vetmed.R
import com.example.vetmed.feature_animal.data.repository.Animals
import com.example.vetmed.feature_animal.presentation.components.AnimalContent
import com.example.vetmed.feature_animal.presentation.components.EmptyPage
import com.example.vetmed.feature_animal.util.RequestState

private const val TAG = "AnimalScreen"
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalScreen(
    animals: Animals,
    navigateToAddAnimalScreen: () -> Unit
) {

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),

        topBar = {
            AnimalTopBar()
        },
        content = {

            when (animals) {
                is RequestState.Success -> {
                    AnimalContent(paddingValues = it,animals = animals.data, onClick = {})
                }

                is RequestState.Error -> {
                    EmptyPage(
                        title = "Error",
                        subtitle = "${animals.error.message}"
                    )
                }

                is RequestState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    Log.d(TAG, "AnimalScreen: No NO NO")

                }
            }

        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddAnimalScreen) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.add_new_animal),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalTopBar() {
    TopAppBar(
        title = {
            Text("Animals")
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.date_icon),
                    tint = MaterialTheme.colorScheme.onSurface
                )

            }
        }
    )

}