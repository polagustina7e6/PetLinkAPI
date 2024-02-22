package com.petlink.database.dao

import com.petlink.models.UserAdditionalInfo

interface UserAdditionalInfoDAO {

    suspend fun updateUserAdditionalInfoByUserId(
        userId: Int,
        age: Int,
        city: String,
        slogan: String,
        description: String,
        foster: Boolean,
        imgId: String
    ): UserAdditionalInfo?
}