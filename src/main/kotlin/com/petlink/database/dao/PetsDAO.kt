package com.petlink.database.dao

import com.petlink.models.AdoptionRequest
import com.petlink.models.Pet

interface PetsDAO {
    suspend fun insertPet(
        userId:Int,
        inAdoption:Boolean,
        name:String,
        type:String,
        gender:String,
        breed:String,
        castrated:Boolean,
        medHistId:String,
        imgId:String
    ): Pet?
    suspend fun getPets(): List<Pet>
    suspend fun getPetsInAdoption(): List<Pet>
    suspend fun getPetsByUserId(userId: Int): List<Pet>
    suspend fun insertAdoptionRequest(fullname: String, petId: Int): AdoptionRequest?
    suspend fun getAdoptionRequestsForPet(petId: Int): List<String>

}