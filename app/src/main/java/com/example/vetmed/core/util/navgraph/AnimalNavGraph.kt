package com.example.vetmed.core.util.navgraph

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.vetmed.core.util.Screen
import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_animal.domain.model.GalleryImage
import com.example.vetmed.feature_animal.domain.model.GalleryState
import com.example.vetmed.feature_animal.domain.model.rememberGalleryState
import com.example.vetmed.feature_animal.presentation.add_animal.AddAnimal
import com.example.vetmed.feature_animal.presentation.AnimalScreen
import com.example.vetmed.feature_animal.presentation.AnimalViewModel
import com.example.vetmed.feature_animal.presentation.add_animal.AddAnimalViewModel


@Composable
fun AnimalNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Animal.route
    ) {
        animalRoute(navigateToAddAnimalScreen = {
            navController.popBackStack()
            navController.navigate(Screen.AddAnimal.route)
        },
            navigateToAddAnimalScreenWithArgs = { animalId ->
                navController.navigate(Screen.AddAnimal.passAnimalId(animalId = animalId))
            })
        addAnimalRoute(
            onBackPressed = {
                navController.popBackStack()
                navController.navigate(Screen.Animal.route)
            }
        )
    }
}

fun NavGraphBuilder.animalRoute(
    navigateToAddAnimalScreen: () -> Unit,
    navigateToAddAnimalScreenWithArgs: (String) -> Unit
) {

    composable(route = Screen.Animal.route) {
        val animalViewModel: AnimalViewModel = hiltViewModel()
        val animals by animalViewModel.animals
        AnimalScreen(
            animals = animals,
            navigateToAddAnimalScreen = navigateToAddAnimalScreen,
            navigateToAddAnimalScreenWithArgs = navigateToAddAnimalScreenWithArgs,
            dateIsSelected = animalViewModel.dateIsSelected,
            onDateSelected = { animalViewModel.getAnimals(zonedDateTime = it) },
            onDateReset = { animalViewModel.getAnimals() }
        )
    }
}


fun NavGraphBuilder.addAnimalRoute(
    onBackPressed: () -> Unit
) {
    composable(route = Screen.AddAnimal.route) {
        val addAnimalViewModel: AddAnimalViewModel = hiltViewModel()
        val uiState = addAnimalViewModel.uiState
        val context = LocalContext.current
        val galleryState = addAnimalViewModel.galleryState

        LaunchedEffect(key1 = uiState) {
            Log.d("SelectedAnimal", "${uiState.selectedAnimalId} ")
        }
        AddAnimal(
            uiState = uiState,
            onDeleteConfirmed = {
                addAnimalViewModel.deleteAnimal(
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "Deleted!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    },
                    onError = { error ->
                        Toast.makeText(
                            context,
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            },
            onBackPressed = onBackPressed,
            onAnimalNameChange = { addAnimalViewModel.setAnimalName(animalName = it) },
            onDescriptionChange = { addAnimalViewModel.setDescription(description = it) },
            onSavedClicked = {
                addAnimalViewModel.upsertAnimal(
                    animal = it,
                    onSuccess = { onBackPressed() },
                    onError = { error ->
                        Toast.makeText(
                            context,
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                )
            },
            onDateTimeUpdated = {
                addAnimalViewModel.updateDateTime(zonedDateTime = it)
            },
            galleryState = galleryState,
            onImageSelect = {
                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"

                Log.d("AddAnimalViewModel", "$it ")
                addAnimalViewModel.addImage(
                    image = it,
                    imageType = type
                )

            },
            onImageDeleteClicked = {
                galleryState.removeImage(it)
            }
        )
    }
}
