package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.PetAdditionalInfoDAO
import com.petlink.models.PetAdditionalInfo
import com.petlink.models.Pets
import com.petlink.models.additional_info_pet
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PetAdditionalInfoRepository: PetAdditionalInfoDAO {
    private fun resultRowToPetAdditionalInfo(row: ResultRow) = PetAdditionalInfo(
        id = row[additional_info_pet.id],
        petId = row[additional_info_pet.petId],
        age = row[additional_info_pet.age],
        weight = row[additional_info_pet.weight],
        size = row[additional_info_pet.size],
        description = row[additional_info_pet.description],
    )

    override suspend fun getAllInfoByPetId(petId: Int): PetAdditionalInfo?  = dbQuery {
        additional_info_pet.select(additional_info_pet.petId eq petId).mapNotNull { resultRowToPetAdditionalInfo(it) }.singleOrNull()
    }

    override suspend fun insertPetInfoAdditional(
        petId: Int,
        age: Int,
        weight: Double,
        size: String,
        description: String
    ): PetAdditionalInfo? = dbQuery{
        val insertStatement = Pets.insert {
            it[additional_info_pet.petId] = petId
            it[additional_info_pet.age] = age
            it[additional_info_pet.weight] = weight
            it[additional_info_pet.size] = size
            it[additional_info_pet.description] = description
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPetAdditionalInfo)
    }
}