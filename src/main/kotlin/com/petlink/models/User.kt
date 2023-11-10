package com.petlink.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val id: Int,
    val name: String,
    val dni: String,
    val phone: String,
    val email: String,
    val password: String,
    val imgProfile: String
)

object Users : Table(){
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val dni = varchar("dni", 9)
    val phone = varchar("phone", 255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val imgProfile = varchar("img_profile", 255)


    override val primaryKey = PrimaryKey(id)
}
