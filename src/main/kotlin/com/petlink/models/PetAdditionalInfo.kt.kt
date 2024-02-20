package com.petlink.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class PetAdditionalInfo(
    val id: Int,
    val petId: Int,
    val age: Int,
    val weight: Double,
    val size: String,
    val description: String
)

object additional_info_pet : Table(){
    val id = integer("id").autoIncrement()
    val petId = integer("info_pet_id")
    val age = integer("age")
    val weight = double("weight")
    val size = varchar("size", 100)
    val description = varchar("description", 255)

    override val primaryKey = PrimaryKey(id)
}