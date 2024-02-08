package com.petlink.database.dao

import com.petlink.models.AdoptionRequest
import com.petlink.models.AdoptionRequests
import com.petlink.models.Users

interface RequestsDAO {
    suspend fun insertAdoptionRequest(requestingUserId: Int, petId: Int): Boolean
    suspend fun deleteAdoptionRequest(requestingUserId: Int, petId: Int): Boolean
    suspend fun getAdoptionRequestsForPet(petId: Int): List<AdoptionRequest>
    suspend fun existsAdoptionRequest(petId: Int, requestingUserId: Int): Boolean
}