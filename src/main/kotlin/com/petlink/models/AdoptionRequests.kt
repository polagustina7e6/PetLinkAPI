package com.petlink.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class AdoptionRequest(
    val id: Int? = null,
    val fullname: String,
    val petId: Int
)

object AdoptionRequests : Table() {
    val id = integer("id").autoIncrement()
    val fullname = varchar("fullname", 255)
    val petId = integer("petid")

    override val primaryKey = PrimaryKey(id)
}