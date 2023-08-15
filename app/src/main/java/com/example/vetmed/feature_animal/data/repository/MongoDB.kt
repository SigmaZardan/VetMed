package com.example.vetmed.feature_animal.data.repository

import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_authentication.presentation.util.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB : MongoRepository {

    private lateinit var realm: Realm
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    override fun configureRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Animal::class))
                .initialSubscriptions() { sub ->
                    add(
                        query = sub.query("ownerId == $0", user.id),
                        name = "User's animal"
                    )
                }
                .log(LogLevel.ALL)
                .build()

            realm = Realm.open(config)
        }
    }
}