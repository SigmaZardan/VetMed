package com.example.vetmed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.vetmed.core.util.Screen
import com.example.vetmed.core.util.navgraph.SetupNavGraph
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_authentication.presentation.util.Constants.APP_ID
import com.example.vetmed.ui.theme.VetMedTheme
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            VetMedTheme() {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = getStartDestination(),
                    navController = navController
                )
            }
        }
    }

    private fun getStartDestination(): String {
        val user = App.create(APP_ID).currentUser

        return if (user != null && user.loggedIn) Screen.Home.route else Screen.Login.route
    }

}

