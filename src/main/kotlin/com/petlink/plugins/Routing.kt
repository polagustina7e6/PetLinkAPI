package com.petlink.plugins

import com.petlink.routes.petsRouting
import com.petlink.routes.usersRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        petsRouting()
        usersRouting()
    }
}
