package com.petlink.database.dao

import com.petlink.models.PetAdditionalInfo

interface PetAdditionalInfoDAO {
    suspend fun getAllInfoByPetId(petId: Int): PetAdditionalInfo?
    suspend fun insertPetInfoAdditional(petId: Int, age: Int, weight: Double, size: String, description: String): PetAdditionalInfo?
}