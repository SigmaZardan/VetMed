package com.example.vetmed.feature_authentication.data

import com.example.vetmed.feature_animal.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.Instant

class User : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var owner_id: String = ""
    var userName: String = ""
    var email: String = ""
    var address: String = ""
    var profile: String = ""
    var description: String = ""
    var isVet: Boolean = false
    var isAvailable: Boolean = false
    var vetTickets: RealmList<String> = realmListOf()
    var date: RealmInstant = Instant.now().toRealmInstant()
    var appointments: RealmList<String> = realmListOf()
    var room: String = ""
}