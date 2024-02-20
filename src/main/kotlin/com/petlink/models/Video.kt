package com.petlink.models



import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Video(
    val id:Int = 0,
    val userId:Int,
    val title: String,
    val youtubeUrl: String
)

object Videos : Table(){
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
    val title = varchar("title", 255)
    val youtubeUrl = varchar("youtube_url", 255)


    override val primaryKey = PrimaryKey(id)
}