package com.example.vetmed.feature_animal.presentation.add_animal

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetmed.feature_animal.data.database.ImageToDeleteDao
import com.example.vetmed.feature_animal.data.database.ImageToUploadDao
import com.example.vetmed.feature_animal.data.database.entity.ImageToDelete
import com.example.vetmed.feature_animal.data.database.entity.ImageToUpload
import com.example.vetmed.feature_animal.data.repository.MongoDB
import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_animal.domain.model.GalleryImage
import com.example.vetmed.feature_animal.domain.model.GalleryState
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_animal.util.toRealmInstant
import com.example.vetmed.feature_authentication.presentation.util.Constants.ADD_ANIMAL_SCREEN_ARGUMENT_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import fetchImagesFromFirebase
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.BsonObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AddAnimalViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val imageToUploadDao: ImageToUploadDao,
    private val imageToDeleteDao: ImageToDeleteDao
) : ViewModel(

) {

    val galleryState = GalleryState()
    var uiState by mutableStateOf(UiState())
        private set


    init {
        getAnimalIdArguments()
        fetchSelectedAnimal()
    }


    private fun getAnimalIdArguments() {
        uiState = uiState.copy(
            selectedAnimalId = savedStateHandle.get<String>(
                key = ADD_ANIMAL_SCREEN_ARGUMENT_KEY
            )
        )
    }

    private fun fetchSelectedAnimal() {
        if (uiState.selectedAnimalId != null) {
            viewModelScope.launch(Dispatchers.Main) {
                MongoDB.getSelectedAnimal(
                    animalId = BsonObjectId.Companion.invoke(uiState.selectedAnimalId!!)
                )
                    .catch {
                        emit(RequestState.Error(Exception("Animal is already deleted!")))
                    }
                    .collect { animal ->
                        if (animal is RequestState.Success) {
                            setAnimal(animal = animal.data)
                            setAnimalName(animalName = animal.data.animalName)
                            setDescription(description = animal.data.description)
                            fetchImagesFromFirebase(
                                remoteImagePaths = animal.data.images,
                                onImageDownload = { downloadedImage ->
                                    galleryState.addImage(
                                        GalleryImage(
                                            image = downloadedImage,
                                            remoteImagePath = extractImagePath(
                                                fullImageUrl = downloadedImage.toString()
                                            ),
                                        )
                                    )
                                }
                            )
                        }
                    }

            }
        }
    }

    private fun setAnimal(animal: Animal) {
        uiState = uiState.copy(selectedAnimal = animal)
    }

    fun setAnimalName(animalName: String) {
        uiState = uiState.copy(animalName = animalName)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime) {
        uiState = uiState.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
    }


    fun upsertAnimal(
        animal: Animal,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedAnimalId != null) {
                updateAnimal(animal = animal, onSuccess = onSuccess, onError = onError)
            } else {
                insertAnimal(animal = animal, onSuccess = onSuccess, onError = onError)
            }

        }


    }


    private suspend fun insertAnimal(
        animal: Animal,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val result = MongoDB.insertAnimal(animal = animal.apply {
            if (uiState.updatedDateTime != null) {
                date = uiState.updatedDateTime!!
            }
        })
        if (result is RequestState.Success) {
            uploadImagesToFirebase()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }

    }


    private suspend fun updateAnimal(
        animal: Animal,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val result =
            MongoDB.updateAnimal(animal = animal.apply {
                _id = BsonObjectId.Companion.invoke(uiState.selectedAnimalId!!)
                date =
                    if (uiState.updatedDateTime != null) uiState.updatedDateTime!! else uiState.selectedAnimal!!.date
            })
        if (result is RequestState.Success) {
            uploadImagesToFirebase()
            deleteImagesFromFirebase()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            withContext(Dispatchers.Main) {
                onError(result.error.message.toString())
            }
        }

    }


    fun deleteAnimal(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedAnimalId != null) {
                val result =
                    MongoDB.deleteAnimal(id = BsonObjectId.invoke(uiState.selectedAnimalId!!))
                if (result is RequestState.Success) {
                    withContext(Dispatchers.Main) {
                        uiState.selectedAnimal?.let { deleteImagesFromFirebase(images = it.images) }
                        onSuccess()
                    }
                } else if (result is RequestState.Error) {
                    withContext(Dispatchers.Main) {
                        onError(result.error.message.toString())
                    }
                }
            }
        }

    }


    fun addImage(image: Uri, imageType: String) {
        val remoteImagePath = "images/${FirebaseAuth.getInstance().currentUser?.uid}/" +
                "${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
        Log.d("AddAnimalViewModel", remoteImagePath)
        galleryState.addImage(
            GalleryImage(
                image = image,
                remoteImagePath = remoteImagePath
            )
        )
    }

    private fun uploadImagesToFirebase() {
        val storage = FirebaseStorage.getInstance().reference
        galleryState.images.forEach { galleryImage ->
            val imagePath = storage.child(galleryImage.remoteImagePath)
            imagePath.putFile(galleryImage.image)
                .addOnProgressListener {
                    val sessionUri = it.uploadSessionUri
                    if (sessionUri != null) {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToUploadDao.addImageToUpload(
                                ImageToUpload(
                                    remoteImagePath = galleryImage.remoteImagePath,
                                    imageUri = galleryImage.image.toString(),
                                    sessionUri = sessionUri.toString()
                                )
                            )
                        }

                    }
                }
        }
    }

    private fun deleteImagesFromFirebase(images: List<String>? = null) {
        val storage = FirebaseStorage.getInstance().reference
        if (images != null) {
            images.forEach { remotePath ->
                storage.child(remotePath).delete()
                    .addOnFailureListener {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToDeleteDao.addImageToDelete(
                                ImageToDelete(remoteImagePath = remotePath)
                            )
                        }

                    }
            }
        } else {
            galleryState.imagesToBeDeleted.map { it.remoteImagePath }.forEach { remotePath ->
                storage.child(remotePath).delete()
                    .addOnFailureListener {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToDeleteDao.addImageToDelete(
                                ImageToDelete(remoteImagePath = remotePath)
                            )
                        }

                    }
            }

        }

    }

    private fun extractImagePath(fullImageUrl: String): String {
        val chunks = fullImageUrl.split("%2F")
        val imageName = chunks[2].split("?").first()
        return "images/${Firebase.auth.currentUser?.uid}/$imageName"
    }
}


data class UiState(
    val selectedAnimalId: String? = null,
    val selectedAnimal: Animal? = null,
    val animalName: String = "",
    val description: String = "",
    val updatedDateTime: RealmInstant? = null
)