package com.example.vetmed.feature_animal.data.repository

import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_animal.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime


typealias Animals = RequestState<Map<LocalDate, List<Animal>>>

interface MongoRepository {
    fun configureRealm()
    fun getAllAnimals(): Flow<Animals>
    fun getFilteredAnimals(zonedDateTime: ZonedDateTime): Flow<Animals>
    fun getSelectedAnimal(animalId: ObjectId): Flow<RequestState<Animal>>
    suspend fun insertAnimal(animal: Animal): RequestState<Animal>
    suspend fun updateAnimal(animal: Animal): RequestState<Animal>
    suspend fun deleteAnimal(id: ObjectId): RequestState<Animal>
}