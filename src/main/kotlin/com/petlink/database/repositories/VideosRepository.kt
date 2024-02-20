package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.VideosDAO


import com.petlink.models.Video
import com.petlink.models.Videos
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class VideosRepository: VideosDAO{
    private fun resultRowToVideo(row: ResultRow) = Video(
        id = row[Videos.id],
        userId = row[Videos.userId],
        title = row[Videos.title],
        youtubeUrl = row[Videos.youtubeUrl]
    )
    override suspend fun getAllVideos(): List<Video> = dbQuery{
        Videos.selectAll().map(::resultRowToVideo)
    }

    override suspend fun getVideoById(videoId: Int): List<Video> = dbQuery{
        Videos.select { Videos.id eq videoId}.map(::resultRowToVideo)
    }
    override suspend fun addVideo(userId: Int, title: String, youtubeUrl: String): Video? = dbQuery{
        val insertStatement = Videos.insert {
            it[Videos.userId] = userId
            it[Videos.title] = title
            it[Videos.youtubeUrl] = youtubeUrl
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToVideo)
    }
}