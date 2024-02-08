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
import org.jetbrains.exposed.sql.transactions.transaction

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

    override suspend fun getLastPetId(): Int? = dbQuery {
        val resultRow = transaction {
            Pets.selectAll()
                .orderBy(Pets.id, SortOrder.DESC)
                .limit(1)
                .singleOrNull()
        }
        resultRow?.get(Pets.id)
    }


    override suspend fun getPetsByUserId(userId: Int): List<Pet> = dbQuery {
        Pets.select {Pets.userId eq userId}.map(::resultRowToPet)
    }

    override suspend fun getPetsInAdoptionByUserId(userId: Int): List<Pet> {
        return transaction {
            Pets.select { (Pets.userId eq userId) and (Pets.inAdoption eq true) }
                .map(::resultRowToPet)
        }
    }
    suspend fun getPetsByBreed(breed: String): List<Pet> = dbQuery {
        Pets.select { Pets.breed.lowerCase() like "%${breed.lowercase()}%" }.map(::resultRowToPet)
    }

    override suspend fun updateAdoptionStatus(petId: Int, newStatus: Boolean): Boolean = dbQuery {
        val originalStatus = Pets.select { Pets.id eq petId }.mapNotNull { it[Pets.inAdoption] }.singleOrNull()

        if (originalStatus != null) {
            if (originalStatus != newStatus) {
                Pets.update({ Pets.id eq petId }) {
                    it[Pets.inAdoption] = newStatus
                }
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    override suspend fun updateOwnerPet(petId: Int, userId: Int): Boolean = dbQuery {
        transaction {
            val updatedRows = Pets.update({ Pets.id eq petId }) {
                it[Pets.userId] = userId
                it[Pets.inAdoption] = false
            }
            updatedRows > 0
        }
    }


    override suspend fun updateCastratedStatus(petId: Int, castratedStatus: Boolean): Pet? = dbQuery{
        Pets.update({Pets.id eq petId}) {
            it[Pets.castrated] = castratedStatus
        }
        Pets.select { Pets.id eq petId }.mapNotNull(::resultRowToPet).singleOrNull()
    }

    override suspend fun getPetsByType(type: String): List<Pet> = dbQuery {
        Pets.select { Pets.type.lowerCase() eq type }.map(::resultRowToPet)
    }

    override suspend fun getMedFromPet(petId: Int): String? = dbQuery {
        Pets
            .select{Pets.id eq petId}
            .mapNotNull { it[Pets.medHistId] }
            .singleOrNull()
    }
}