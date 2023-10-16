package com.petlink.routes

import com.petlink.database.repositories.PetsRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val petsRepository = PetsRepository()

fun Route.petsRouting(){
    route("/pets"){
        get("/inadoption"){
            val petsInAdoption = petsRepository.getPetsInAdoption()
            call.respond(petsInAdoption)
        }
    }
}