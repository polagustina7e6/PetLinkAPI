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
    suspend fun updateAdoptionStatus(petId: Int, status: Boolean): Boolean
    suspend fun updateOwnerPet(petId: Int, userId: Int)
    suspend fun updateCastratedStatus(petId: Int, status: Boolean): Pet?
}