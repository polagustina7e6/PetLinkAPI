package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.PetAdditionalInfoDAO
import com.petlink.models.AdditionalInfoPet
import com.petlink.models.PetAdditionalInfo
import com.petlink.models.Pets
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PetAdditionalInfoRepository: PetAdditionalInfoDAO {
    private fun resultRowToPetAdditionalInfo(row: ResultRow) = PetAdditionalInfo(
        id = row[AdditionalInfoPet.id],
        petId = row[AdditionalInfoPet.petId],
        age = row[AdditionalInfoPet.age],
        weight = row[AdditionalInfoPet.weight],
        size = row[AdditionalInfoPet.size],
        description = row[AdditionalInfoPet.description],
    )

    override suspend fun getAllInfoByPetId(petId: Int): PetAdditionalInfo?  = dbQuery {
        AdditionalInfoPet.select(AdditionalInfoPet.petId eq petId).mapNotNull { resultRowToPetAdditionalInfo(it) }.singleOrNull()
    }

    override suspend fun insertPetInfoAdditional(
        petId: Int,
        age: Int,
        weight: Double,
        size: String,
        description: String
    ): PetAdditionalInfo? = dbQuery {
        val insertStatement = AdditionalInfoPet.insert {
            it[AdditionalInfoPet.petId] = petId
            it[AdditionalInfoPet.age] = age
            it[AdditionalInfoPet.weight] = weight
            it[AdditionalInfoPet.size] = size
            it[AdditionalInfoPet.description] = description
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPetAdditionalInfo)
    }
}