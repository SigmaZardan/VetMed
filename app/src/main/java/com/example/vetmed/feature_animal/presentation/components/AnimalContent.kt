package com.example.vetmed.feature_animal.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.vetmed.feature_animal.domain.model.Animal
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimalContent(
    paddingValues: PaddingValues,
    animals: Map<LocalDate, List<Animal>>,
    onClick: (String) -> Unit
) {

    if (animals.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 14.dp)
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            animals.forEach { (localDate, animals) ->
                stickyHeader(key = localDate) {
                    DateHeader(localDate = localDate)
                }
                items(items = animals, key = {
                    it._id.toString()
                }) {
                    AnimalHolder(animal = it, onHolderClick = onClick)
                }
            }
        }
    } else {
        EmptyPage()
    }


}