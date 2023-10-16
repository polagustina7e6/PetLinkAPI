package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.PetsDAO
import com.petlink.models.Pet
import com.petlink.models.Pets
import org.jetbrains.exposed.sql.*

class PetsRepository : PetsDAO {
    private fun resultRowToUser(row: ResultRow) = Pet(
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
        id: Int,
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
            it[Pets.id] = id
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
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)

    }

    override suspend fun getPetsInAdoption(): List<Pet> = dbQuery {
        Pets.select { Pets.inAdoption eq Op.TRUE }.map(::resultRowToUser)
    }
}