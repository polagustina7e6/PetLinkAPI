package com.petlink.database.dao

import com.petlink.models.AdoptionRequest

interface RequestsDAO {
    suspend fun insertAdoptionRequest(
        fullname: String,
        petId: Int
    ): AdoptionRequest?

    suspend fun getAdoptionRequestsForPet(petId: Int): List<String>
    suspend fun deleteAdoptionRequest(requestId: Int): Boolean
    suspend fun getAdoptionRequestId(petId: Int, fullname: String): Int?
}