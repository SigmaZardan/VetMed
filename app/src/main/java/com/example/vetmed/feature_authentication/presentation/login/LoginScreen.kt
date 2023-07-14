package com.example.vetmed.feature_authentication.presentation.login

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.vetmed.feature_authentication.presentation.util.Constants.CLIENT_ID
import com.example.vetmed.ui.theme.VetMedTheme
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authenticated: Boolean,
    oneTapState: OneTapSignInState,
    googleButtonLoadingState: Boolean,
    messageState: MessageBarState,
    onSignInButtonClick: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateToHome: () -> Unit,
    onGoogleAccountAdditionSuccess: () -> Unit,
    onGoogleAccountAdditionUnSuccess: () -> Unit,
) {
    val context = LocalContext.current
    val chosenAccount =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
//                Toast.makeText(context, "Successfully added google account", Toast.LENGTH_SHORT)
                onGoogleAccountAdditionSuccess()

            } else {
                onGoogleAccountAdditionUnSuccess()}

        }

    Scaffold(
        content = {
            ContentWithMessageBar(messageBarState = messageState) {
                LoginContent(
                    loadingState = googleButtonLoadingState,
                    onSignInButtonClick = {
                        if (!hasGoogleAccount(context = context)) {
                            val intent = AccountManager.newChooseAccountIntent(
                                null,
                                null,
                                arrayOf("com.google"),
                                null,
                                null,
                                null,
                                null
                            )
                            chosenAccount.launch(intent)
                        } else {
                            onSignInButtonClick()
                        }
                    }
                )
            }
        }

    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("Auth", tokenId)
            onTokenIdReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
            messageState.addError(Exception(message))
        }
    )
    LaunchedEffect( authenticated) {
        if (authenticated) {
            navigateToHome()
        }
    }



}


private fun hasGoogleAccount(context: Context): Boolean {
    val accountManager = AccountManager.get(context)
    val accounts = accountManager.getAccountsByType(null)
    return accounts.any { it.type == "com.google" }
}

@Preview
@Composable
fun LoginScreenPreview() {
    VetMedTheme {
        LoginScreen(
            authenticated =false,
            oneTapState = OneTapSignInState(),
            messageState = MessageBarState(),
            googleButtonLoadingState = false,
            onSignInButtonClick = {},
            onDialogDismissed = {},
            onTokenIdReceived = {},
            navigateToHome = {},
            onGoogleAccountAdditionSuccess = {},
            onGoogleAccountAdditionUnSuccess = {},

        )

    }
}