package com.petlink.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class AdoptionRequest(
    val id: Int? = null,
    val requestingUserId: Int,
    val petId: Int
)

object AdoptionRequests : Table() {
    val id = integer("request_id").autoIncrement()
    val requestingUserId = integer("requesting_user_id")
    val petId = integer("pet_id")

    override val primaryKey = PrimaryKey(id)
}