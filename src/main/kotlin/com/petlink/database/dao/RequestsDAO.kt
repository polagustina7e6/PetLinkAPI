package com.petlink.database.dao

import com.petlink.models.AdoptionRequest

interface RequestsDAO {
    suspend fun insertAdoptionRequest(requestingUserId: Int, petId: Int): Boolean
    suspend fun deleteAdoptionRequest(requestingUserId: Int, petId: Int): Boolean
    suspend fun getAdoptionRequestsForPet(petId: Int)
    suspend fun existsAdoptionRequest(petId: Int, requestingUserId: Int): Boolean
}