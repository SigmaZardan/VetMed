package com.example.vetmed.feature_animal.data.repository

import com.example.vetmed.feature_animal.domain.model.Animal
import com.example.vetmed.feature_animal.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


typealias Animals = RequestState<Map<LocalDate, List<Animal>>>

interface MongoRepository {
    fun configureRealm()
    fun getAllAnimals(): Flow<Animals>
}