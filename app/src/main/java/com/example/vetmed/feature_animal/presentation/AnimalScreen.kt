package com.example.vetmed.feature_animal.presentation

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.vetmed.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalScreen(
    navigateToAddAnimalScreen: () -> Unit
) {

    Scaffold(

        topBar = {

        },
        floatingActionButton =  {
            FloatingActionButton(onClick = navigateToAddAnimalScreen) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.add_new_animal) )
            }
        }

    ) {


    }

}