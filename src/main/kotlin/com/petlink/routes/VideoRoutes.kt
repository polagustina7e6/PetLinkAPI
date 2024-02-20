package com.petlink.routes

import com.petlink.database.repositories.VideosRepository
import com.petlink.models.Pet
import com.petlink.models.Video
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val videosRepository = VideosRepository()

fun Route.videosRouting() {
    route("/videos") {
        get {
            val videos = videosRepository.getAllVideos()
            call.respond(HttpStatusCode.OK, videos)
        }

        get("/{videoId}") {
            val videoId = call.parameters["videoId"]?.toIntOrNull()

            if (videoId != null) {
                val video = videosRepository.getVideoById(videoId)
                if (video.isNotEmpty()) {
                    call.respond(HttpStatusCode.OK, video.first())
                } else {
                    call.respond(HttpStatusCode.NotFound, "Video not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid videoId parameter")
            }
        }

        post {
            try {
                val newvideo = call.receive<Video>()
                val addedVideo = videosRepository.addVideo(newvideo.userId, newvideo.title, newvideo.youtubeUrl)
                call.respond(HttpStatusCode.Created, addedVideo ?: "Failed to add video")
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            }
        }
    }
}