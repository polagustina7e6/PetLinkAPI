package com.petlink.database.dao

import com.petlink.models.Video

interface VideosDAO {
    suspend fun getAllVideos(): List<Video>
    suspend fun getVideoById(videoId: Int): List<Video>
    suspend fun addVideo(userId: Int, title: String, youtubeUrl: String): Video?
}