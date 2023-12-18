package com.petlink.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Pet(
    val id:Int,
    val userId:Int,
    val inAdoption:Boolean,
    val name:String,
    val type:String,
    val gender:String,
    val breed:String,
    val castrated:Boolean,
    val medHistId:String,
    val imgId:String
)

object Pets : Table(){
    val id = integer("pet_id").autoIncrement()
    val userId = integer("pet_user_id")
    val inAdoption = bool("pet_in_adoption")
    val name = varchar("pet_name", 15)
    val type = varchar("pet_type", 10)
    val gender = varchar("pet_gender", 6)
    val breed = varchar("pet_breed", 50)
    val castrated = bool("pet_castrated")
    val medHistId = varchar("pet_med_hist_id", 255)
    val imgId = varchar("pet_img_id", 255)


    override val primaryKey = PrimaryKey(id)
}
