package com.example.vetmed.feature_profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.vetmed.R


@Composable
fun ProfileAndNameSection(
    profilePicUrl: String,
    userName: String,
    email: String
) {

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profilePicUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.default_profile_picture),
                contentDescription = stringResource(R.string.profile_picture),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = userName,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = email,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

}

@Composable
@Preview
fun ProfileAndNameSectionPreview() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://th.bing.com/th/id/OIP.SeEMplDHhzsfqk7GGr3o0wHaFb?pid=ImgDet&rs=1")
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.default_profile_picture),
            contentDescription = stringResource(R.string.profile_picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Bibek Bhujel",
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            )
            Text(
                text = "bibekbhujel077@gmail.com",
                style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            )
        }
    }

}