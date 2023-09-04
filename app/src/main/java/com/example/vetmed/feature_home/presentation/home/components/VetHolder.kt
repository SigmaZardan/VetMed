package com.example.vetmed.feature_home.presentation.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.vetmed.R
import com.example.vetmed.feature_authentication.data.User
import com.example.vetmed.ui.theme.Elevation
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.serialization.Bson


@Composable
fun VetHolder(
    vetUser: User,
    onCallButtonClick: (String) -> Unit
) {

    var isAvailable by remember { mutableStateOf(false) }
    isAvailable = vetUser.isAvailable
    val buttonText = if (isAvailable) "BUY" else "BUSY"
    Row {

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium),
            tonalElevation = Elevation.Level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(vetUser.profile)
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
                            text = vetUser.userName,
                            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                        )
                        Text(
                            text = vetUser.email,
                            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
                        )
                    }

                }
                Text(
                    modifier = Modifier.padding(all = 12.dp),
                    text = vetUser.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Surface(
                        shape = Shapes().extraSmall,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        color = MaterialTheme.colorScheme.surface,

                        ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = LinearOutSlowInEasing
                                    )
                                )
                                .clickable(enabled = isAvailable) { onCallButtonClick(vetUser._id.toHexString()) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.icons8_call_96),
                                contentDescription = "Call Icon",
                                tint = Color.Unspecified,
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = buttonText,
                                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                            )
                        }
                    }

                }

            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun VetHolderPreview() {
    VetHolder(vetUser = User().apply {
        userName = "Vet bibek"
        email = "bibekbhujel@gmail.com"
        description =
            "He is just a man who is trying to do the best in his life , and he will loop after your pets in a good and organized way . he is experienced and he loves to spend time with his wife."

    },
        onCallButtonClick = {}
    )
}
