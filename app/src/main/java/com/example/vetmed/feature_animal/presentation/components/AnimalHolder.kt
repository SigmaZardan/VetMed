package com.example.vetmed.feature_animal.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.ui.theme.Elevation
import io.realm.kotlin.ext.realmListOf
import toInstant

@Composable
fun AnimalHolder(animal: Animal, onHolderClick: (String) -> Unit) {
    var componentHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    var galleryOpened by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) { onHolderClick(animal._id.toString()) }
    ) {
        Spacer(modifier = Modifier.width(14.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}
        Spacer(modifier = Modifier.width(20.dp))
        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.Level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AnimalHeader(animalName = animal.animalName, time = animal.date.toInstant())
                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = animal.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )

                if (animal.images.isNotEmpty()) {
                    ShowGalleryButton(
                        galleryOpened = galleryOpened,
                        onClick = {
                            galleryOpened = !galleryOpened
                        }
                    )
                }

                AnimatedVisibility(visible = galleryOpened) {
                    Column(Modifier.padding(all = 14.dp)) {
                        AnimalGallery(images = animal.images)
                    }

                }
            }
        }
    }
}


@Preview
@Composable
fun AnimalHolderPreview() {
    AnimalHolder(animal = Animal().apply {
        animalName = "Nani"
        images = realmListOf(" ", " ", " ")
        description =
            "It has two beautiful eyes, adorably tiny paws, sharp claws, and two perky ears which are very sensitive to sounds. It has a tiny body covered with smooth fur and it has a furry tail as well. Cats have an adorable face with a tiny nose, a big mouth and a few whiskers under its nose."
    }, onHolderClick = {})

}