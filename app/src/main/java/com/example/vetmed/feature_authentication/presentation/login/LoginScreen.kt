package com.example.vetmed.feature_authentication.presentation.login

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.vetmed.feature_authentication.presentation.util.Constants.CLIENT_ID
import com.example.vetmed.ui.theme.VetMedTheme
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    oneTapState: OneTapSignInState,
    googleButtonLoadingState: Boolean,
    onSignInButtonClick: () -> Unit
) {
    val context = LocalContext.current
    val chosenAccount =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(context, "Successfully added google account", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Unable to add google account", Toast.LENGTH_SHORT).show()
            }
        }

    Scaffold(
        content = {
            LoginContent(
                loadingState = googleButtonLoadingState,
                onSignInButtonClick = {
                    val accountManager = AccountManager.get(context)
                    val accounts = accountManager.getAccountsByType(null)
                    val hasGoogleAccount = accounts.any { it.type == "com.google" }

                    if (!hasGoogleAccount) {
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
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("AUTH", tokenId)
        },
        onDialogDismissed = { message ->
            Log.d("AUTH", message)
        }
    )


}

@Preview
@Composable
fun LoginScreenPreview() {
    VetMedTheme {
        LoginScreen(oneTapState = OneTapSignInState(), googleButtonLoadingState = false) {

        }

    }
}