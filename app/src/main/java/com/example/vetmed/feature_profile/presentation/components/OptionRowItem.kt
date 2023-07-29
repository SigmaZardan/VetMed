package com.example.vetmed.feature_profile.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vetmed.R
import com.example.vetmed.ui.theme.VetMedTheme

@Composable
fun OptionRowItem(
    iconId: Int,
    option: String,
    onOptionClicked: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
       shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onOptionClicked()
                }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = option,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 10.dp),
                        tint = MaterialTheme.colorScheme.onSurface

                    )
                    Text(
                        text = option,
                        style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    )
                }

            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = stringResource(R.string.arrow_head),
                modifier = Modifier
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface

            )
        }

    }

}

@Preview
@Composable

fun OptionRowPreview() {
    VetMedTheme {
        OptionRowItem(
            iconId = R.drawable.privacy_policy,
            option = "Privacy Policy",
            onOptionClicked = {}

        )
    }

}