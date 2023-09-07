package com.example.vetmed.feature_animal.data.repository

import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime


typealias Animals = RequestState<Map<LocalDate, List<Animal>>>
typealias  Users = RequestState<List<User>>
typealias VetUsers = RequestState<List<String>>

interface MongoRepository {
    fun configureRealm()
    fun getAllAnimals(): Flow<Animals>
    fun getAllVets(): Flow<Users>
    fun getAllVetsWithTickets(): Flow<VetUsers>
    fun getVetWithGivenId(id: BsonObjectId): Flow<RequestState<User>>
    fun getFilteredAnimals(zonedDateTime: ZonedDateTime): Flow<Animals>
    fun getSelectedAnimal(animalId: ObjectId): Flow<RequestState<Animal>>
    suspend fun insertAnimal(animal: Animal): RequestState<Animal>
    suspend fun insertUser(user: User): RequestState<User>
    suspend fun updateAnimal(animal: Animal): RequestState<Animal>
    suspend fun updateUser(id: String): RequestState<Boolean>
    suspend fun isVet(): RequestState<Boolean>
    suspend fun deleteAnimal(id: ObjectId): RequestState<Animal>
    suspend fun getAllUsers(): Flow<Users>
}