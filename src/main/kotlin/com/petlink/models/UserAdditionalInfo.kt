package com.petlink.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class UserAdditionalInfo(
    val userId: Int,
    val age: Int,
    val city: String,
    val slogan: String,
    val description: String,
    val foster: Boolean,
    val imgId: String
)

object  Additional_info_user : Table(){
    val userId = integer("id_user") references Users.id
    val age = integer("age")
    val city = varchar("city", 255)
    val slogan = varchar("slogan", 255)
    val description = varchar("description", 255)
    val foster = bool("foster")
    val imgId = varchar("image_id",255)

    override val primaryKey = PrimaryKey(userId)
}