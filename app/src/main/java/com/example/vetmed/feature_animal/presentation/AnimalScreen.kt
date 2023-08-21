package com.example.vetmed.feature_animal.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vetmed.R
import com.example.vetmed.feature_animal.data.repository.Animals
import com.example.vetmed.feature_animal.presentation.components.AnimalContent
import com.example.vetmed.feature_animal.presentation.components.EmptyPage
import com.example.vetmed.feature_animal.util.RequestState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

private const val TAG = "AnimalScreen"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalScreen(
    animals: Animals,
    navigateToAddAnimalScreen: () -> Unit,
    navigateToAddAnimalScreenWithArgs: (String) -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = 55.dp),

        topBar = {
            AnimalTopBar(
                scrollBehavior = scrollBehavior,
                dateIsSelected = dateIsSelected,
                onDateSelected = onDateSelected,
                onDateReset = onDateReset
            )
        },
        content = {

            when (animals) {
                is RequestState.Success -> {
                    AnimalContent(
                        paddingValues = it,
                        animals = animals.data,
                        onClick = navigateToAddAnimalScreenWithArgs
                    )
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
            FloatingActionButton(
                onClick = navigateToAddAnimalScreen
            ) {
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
fun AnimalTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit

) {

    val dateDialog = rememberSheetState()
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text("Animals")
        },
        actions = {
            if (dateIsSelected) {
                IconButton(
                    onClick = onDateReset
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

            } else {
                IconButton(
                    onClick = { dateDialog.show() }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.date_icon),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                }
            }
        }


    )

    CalendarDialog(
        state = dateDialog, selection = CalendarSelection.Date { localDate ->
            pickedDate = localDate
            onDateSelected(
                ZonedDateTime.of(
                    pickedDate,
                    LocalTime.now(),
                    ZoneId.systemDefault()
                )
            )
        },
        config = CalendarConfig(monthSelection = true, yearSelection = true)
    )

}