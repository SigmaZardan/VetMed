package com.example.vetmed.feature_payment.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vetmed.R
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBackPressed: () -> Unit,
    onPayButtonClick: () -> Unit,
    messageBarState: MessageBarState
) {
    Scaffold(
        topBar = {
            PaymentScreenTopBar(
                onBackPressed = onBackPressed
            )
        },
    ) {
        ContentWithMessageBar(messageBarState = messageBarState) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.weight(1f),
                    painter = painterResource(id = R.drawable.khalti_logo),
                    contentDescription = "Khalti Logo"
                )
                Surface(
                    modifier = Modifier
                        .clickable { onPayButtonClick() }
                        .padding(15.dp),
                    shape = Shapes().extraSmall,
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    color = MaterialTheme.colorScheme.surface
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
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Default.Payment,
                            contentDescription = "Pay Icon",
                            tint = Color.Unspecified,
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PAY",
                            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                        )
                    }
                }
            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreenTopBar(
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate Back Button"
                )
            }
        },
        title = {
            Text(
                text = "KHALTI PAYMENT",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        })
}


@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        onBackPressed = { /*TODO*/ },
        onPayButtonClick = {},
        messageBarState = MessageBarState()
    )

}