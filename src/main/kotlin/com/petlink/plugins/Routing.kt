package com.petlink.plugins

import com.petlink.routes.petsRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/petlink"){
            petsRouting()
            get {
                call.respondText("Welcome Welcome")
            }
        }
    }
}
