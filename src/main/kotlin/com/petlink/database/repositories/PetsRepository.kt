package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.PetsDAO
import com.petlink.models.AdoptionRequest
import com.petlink.models.AdoptionRequests

import com.petlink.models.Pet
import com.petlink.models.Pets
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import org.jetbrains.exposed.sql.*

class PetsRepository : PetsDAO {
    private fun resultRowToPet(row: ResultRow) = Pet(
        id = row[Pets.id],
        userId = row[Pets.userId],
        inAdoption = row[Pets.inAdoption],
        name = row[Pets.name],
        type = row[Pets.type],
        gender = row[Pets.gender],
        breed = row[Pets.breed],
        castrated = row[Pets.castrated],
        medHistId = row[Pets.medHistId],
        imgId = row[Pets.imgId]
    )
    private fun resultRowToAdoptionRequest(row: ResultRow) = AdoptionRequest(
        id = row[AdoptionRequests.id],
        fullname = row[AdoptionRequests.fullname],
        petId = row[AdoptionRequests.petId]
    )

    override suspend fun insertPet(
        userId: Int,
        inAdoption: Boolean,
        name: String,
        type: String,
        gender: String,
        breed: String,
        castrated: Boolean,
        medHistId: String,
        imgId: String
    ): Pet? = dbQuery{
        val insertStatement = Pets.insert {
            it[Pets.userId] = userId
            it[Pets.inAdoption] = inAdoption
            it[Pets.name] = name
            it[Pets.type] = type
            it[Pets.gender] = gender
            it[Pets.breed] = breed
            it[Pets.castrated] = castrated
            it[Pets.medHistId] = medHistId
            it[Pets.imgId] = imgId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPet)
    }

    override suspend fun getPets(): List<Pet> = dbQuery{
        Pets.selectAll().map(::resultRowToPet)
    }

    override suspend fun getPetsInAdoption(): List<Pet> = dbQuery {
        Pets.select { Pets.inAdoption eq Op.TRUE }.map(::resultRowToPet)
    }

    override suspend fun getPetsByUserId(userId: Int): List<Pet> = dbQuery {
        Pets.select {Pets.userId eq userId}.map(::resultRowToPet)
    }
    suspend fun getPetsByBreed(breed: String): List<Pet> = dbQuery {
        Pets.select { Pets.breed.lowerCase() like "%${breed.lowercase()}%" }.map(::resultRowToPet)
    }

    override suspend fun insertAdoptionRequest(fullname: String, petId: Int): AdoptionRequest? = dbQuery {
        val insertStatement = AdoptionRequests.insert {
            it[AdoptionRequests.fullname] = fullname
            it[AdoptionRequests.petId] = petId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAdoptionRequest)
    }
    override suspend fun getAdoptionRequestsForPet(petId: Int): List<String> = dbQuery {
        AdoptionRequests.select { AdoptionRequests.petId eq petId }
            .map { it[AdoptionRequests.fullname] }
    }
}