package com.example.vetmed.feature_animal.data.repository

import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_animal.util.RequestState
import com.example.vetmed.feature_authentication.data.User
import com.example.vetmed.feature_authentication.presentation.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
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
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Animal>("owner_id == $0", user.id),
                        name = "User's animal"
                    )
                    add(
                        query = sub.query<User>("owner_id == $0", user.id),
                        name = "Vetmed user"
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
            realm.write {
                try {
                    val addedUser = copyToRealm(user.apply { owner_id = MongoDB.user.id })
                    RequestState.Success(data = addedUser)
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