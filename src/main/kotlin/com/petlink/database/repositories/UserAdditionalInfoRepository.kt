package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.UserAdditionalInfoDAO
import com.petlink.models.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.select

class UserAdditionalInfoRepository : UserAdditionalInfoDAO {
    private fun resultRowToUserAdditionalInfo(row: ResultRow) = UserAdditionalInfo(
        userId = row[Additional_info_user.userId],
        age = row[Additional_info_user.age],
        city = row[Additional_info_user.city],
        slogan = row[Additional_info_user.slogan],
        description = row[Additional_info_user.description],
        foster = row[Additional_info_user.foster],
        imgId = row[Additional_info_user.imgId]
    )



    suspend fun getUserAdditionalInfoByUserId(userId: Int): UserAdditionalInfo? = dbQuery {
        Additional_info_user.select { Additional_info_user.userId eq userId }.mapNotNull { resultRowToUserAdditionalInfo(it) }.singleOrNull()
    }

    override suspend fun updateUserAdditionalInfoByUserId(
        userId: Int,
        age: Int,
        city: String,
        slogan: String,
        description: String,
        foster: Boolean,
        imgId: String
    ): UserAdditionalInfo? = dbQuery {
        Additional_info_user.update({ Additional_info_user.userId eq userId }) {
            it[Additional_info_user.age] = age
            it[Additional_info_user.city] = city
            it[Additional_info_user.slogan] = slogan
            it[Additional_info_user.description] = description
            it[Additional_info_user.foster] = foster
            it[Additional_info_user.imgId] = imgId
        }
        getUserAdditionalInfoByUserId(userId) // Return updated user additional info
    }
}