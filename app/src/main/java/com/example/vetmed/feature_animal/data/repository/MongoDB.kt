package com.example.vetmed.feature_animal.data.repository

import android.app.DownloadManager.Request
import android.util.Log
import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User
import com.example.vetmed.feature_authentication.presentation.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import okhttp3.internal.wait
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId
import toInstant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object MongoDB : MongoRepository {

    private lateinit var realm: Realm
    private val app = App.create(APP_ID)
    private val user = app.currentUser

    init {
        configureRealm()
    }

    override fun configureRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(User::class, Animal::class))
                .initialSubscriptions(rerunOnOpen = true) { sub ->
                    add(
                        query = sub.query<Animal>("owner_id == $0", user.id),
                        name = "User's animal"
                    )
                    add(
                        query = sub.query<User>("isVet == $0", true),
                        name = "Vetmed user", updateExisting = true
                    )

                    add(
                        query = sub.query<User>("owner_id == $0", user.id),
                        name = "Update only current user", updateExisting = true
                    )
                    add(
                        query = sub.query<User>("isVet == $0", false),
                        name = "Users who are not bet", updateExisting = true
                    )

                }
                .log(LogLevel.ALL)
                .build()

            realm = Realm.open(config)
        }
    }

    override fun getAllAnimals(): Flow<Animals> {
        return if (user != null) {
            try {
                realm.query<Animal>(query = "owner_id == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }

        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }

    override fun getAllVets(): Flow<Users> {
        return if (user != null) {
            try {
                realm.query<User>(query = "isVet == $0", true)
                    .asFlow().map { result ->
                        RequestState.Success(
                            data = result.list
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }

        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }

    override fun getVetWithGivenId(id: ObjectId): Flow<RequestState<User>> {
        return if (user != null) {
            try {
                realm.query<User>(query = "_id == $0 AND isVet == $1", id, true).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }

            } catch (e: Exception) {
                flow {
                    emit(
                        RequestState.Error(e)
                    )
                }
            }
        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }

    override fun getAllVetsWithTickets(): Flow<VetUsers> {
        return if (user != null) {
            try {
                realm.query<User>(
                    query = "owner_id == $0 ", user.id
                ).find().first().vetTickets.asFlow().map { result ->
                    RequestState.Success(
                        data = result.list
                    )
                }

            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getFilteredAnimals(zonedDateTime: ZonedDateTime): Flow<Animals> {
        return if (user != null) {
            try {
                realm.query<Animal>(
                    "owner_id == $0 AND date < $1 AND date > $2",
                    user.id,
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate().plusDays(1),
                            LocalTime.MIDNIGHT
                        ).toEpochSecond(zonedDateTime.offset), 0
                    ),
                    RealmInstant.from(
                        LocalDateTime.of(
                            zonedDateTime.toLocalDate(),
                            LocalTime.MIDNIGHT
                        ).toEpochSecond(zonedDateTime.offset), 0
                    ),
                ).asFlow().map { result ->
                    RequestState.Success(
                        data = result.list.groupBy {
                            it.date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                    )
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedAnimal(animalId: ObjectId): Flow<RequestState<Animal>> {
        return if (user != null) {
            try {
                realm.query<Animal>(query = "_id == $0", animalId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }

            } catch (e: Exception) {
                flow {
                    emit(
                        RequestState.Error(e)
                    )
                }
            }
        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }

    override suspend fun insertAnimal(animal: Animal): RequestState<Animal> {
        return if (user != null) {
            realm.write {
                try {
                    val addedAnimal = copyToRealm(animal.apply { owner_id = user.id })
                    RequestState.Success(data = addedAnimal)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }


    override suspend fun insertUser(user: User): RequestState<User> {
        return if (MongoDB.user != null) {
            // Search equality on the primary key field name
            val userAlreadyPresent: User? =
                realm.query<User>("owner_id == $0", MongoDB.user.id).first().find()

            realm.write {
                try {
                    if (userAlreadyPresent == null) {
                        val addedUser = copyToRealm(user.apply { owner_id = MongoDB.user.id })
                        RequestState.Success(data = addedUser)
                    } else {
                        RequestState.Error(Exception("User already exits "))
                    }

                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }

    }

    override suspend fun updateAnimal(animal: Animal): RequestState<Animal> {
        return if (user != null) {
            realm.write {
                val queriedAnimal = query<Animal>(query = "_id == $0", animal._id).first().find()
                if (queriedAnimal != null) {
                    queriedAnimal.animalName = animal.animalName
                    queriedAnimal.description = animal.description
                    queriedAnimal.images = animal.images
                    queriedAnimal.date = animal.date
                    RequestState.Success(data = queriedAnimal)
                } else {
                    RequestState.Error(Exception("Queried Animal Doesn't Exist"))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateUser(id: String): RequestState<Boolean> {
        return if (user != null) {
            realm.write {
                val queriedUser =
                    query<User>(query = "owner_id == $0 ", user.id).first()
                        .find()
                if (queriedUser != null) {
                    val vetIdToAdd = BsonObjectId.Companion.invoke(id).toHexString()
                    if (!queriedUser.vetTickets.contains(vetIdToAdd)) {
                        queriedUser.vetTickets.add(vetIdToAdd)
                        RequestState.Success(data = true)
                    } else {
                        // Vet ID already exists in the list, do nothing
                        RequestState.Success(data = false)
                    }
                } else {
                    RequestState.Error(Exception("Queried Vet Doesn't Exist"))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    // when you update user based on the addition of the vets into vetTickets similarly we have to update the
    // vet with the user who asked for appointment
    override suspend fun getAllUsers(): Flow<Users> {
        return if (user != null) {
            try {
                val queriedVet = realm.query<User>(query = "owner_id == $0", user.id).first().find()
                realm.query<User>(query = "isVet == $0", false).asFlow().map { result ->
                    Log.d("CheckingUsers", "getAllUsers: ${result.list.size}")
                    val listOfUsers =
                        result.list.filter { it.vetTickets.contains(queriedVet?._id?.toHexString()) }
                    RequestState.Success(data = listOfUsers)
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow {
                emit(RequestState.Error(UserNotAuthenticatedException()))
            }
        }
    }


    override suspend fun isVet(): RequestState<Boolean> {
        return if (user != null) {
            // check if the logged in user is a vet or not
            // get the user based on the id
            val queriedUser = realm.query<User>(query = "owner_id == $0", user.id).first().find()
            if (queriedUser != null) {
                Log.d("IsVet", "isVet:${queriedUser.isVet}")
                RequestState.Success(queriedUser.isVet)
            } else {
                RequestState.Error(Exception("User cannot be fetched"))
            }

        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }


    override suspend fun deleteAnimal(id: ObjectId): RequestState<Animal> {
        return if (user != null) {
            realm.write {
                val animal =
                    query<Animal>(query = "_id == $0 AND owner_id == $1", id, user.id)
                        .first().find()
                if (animal != null) {
                    try {
                        delete(animal)
                        RequestState.Success(data = animal)
                    } catch (e: Exception) {
                        RequestState.Error(e)
                    }
                } else {
                    RequestState.Error(Exception("Animal does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    private class UserNotAuthenticatedException : Exception("User is not logged in")
}