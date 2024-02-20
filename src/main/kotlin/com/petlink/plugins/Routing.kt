package com.petlink.plugins

import com.petlink.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        petsRouting()
        petsAdditionalInfoRouting()
        requestsRouting()
        usersRouting()
        videosRouting()
    }
}
