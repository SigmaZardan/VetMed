package com.example.vetmed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.vetmed.core.util.Screen
import com.example.vetmed.core.util.navgraph.SetupNavGraph
import com.example.vetmed.feature_animal.data.database.ImageToDeleteDao
import com.example.vetmed.feature_animal.data.database.ImageToUploadDao
import com.example.vetmed.feature_authentication.presentation.util.Constants.APP_ID
import com.example.vetmed.ui.theme.VetMedTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retryDeletingImageFromFirebase
import retryUploadingImageToFirebase
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao

    @Inject
    lateinit var imageToDeleteDao: ImageToDeleteDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(this)

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
        cleanupCheck(
            scope = lifecycleScope,
            imageToUploadDao = imageToUploadDao,
        )
    }

    private fun cleanupCheck(
        scope: CoroutineScope,
        imageToUploadDao: ImageToUploadDao,
    ) {
        scope.launch(Dispatchers.IO) {
            val result = imageToUploadDao.getAllImages()
            result.forEach { imageToUpload ->
                retryUploadingImageToFirebase(
                    imageToUpload = imageToUpload,
                    onSuccess = {
                        scope.launch(Dispatchers.IO) {
                            imageToUploadDao.cleanupImage(imageId = imageToUpload.id)
                        }
                    }
                )
            }
            val result2 = imageToDeleteDao.getAllImages()
            result2.forEach { imageToDelete ->
                retryDeletingImageFromFirebase(
                    imageToDelete = imageToDelete,
                    onSuccess = {
                        scope.launch(Dispatchers.IO) {
                            imageToDeleteDao.cleanupImage(imageId = imageToDelete.id)
                        }
                    }
                )
            }
        }


    }


    private fun getStartDestination(): String {
        val user = App.create(APP_ID).currentUser

        return if (user != null && user.loggedIn) Screen.Home.route else Screen.Login.route
    }

}

