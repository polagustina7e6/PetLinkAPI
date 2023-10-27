package com.petlink.routes

import com.petlink.database.repositories.PetsRepository
import com.petlink.models.Pet
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val petsRepository = PetsRepository()

fun Route.petsRouting(){
    route("/pets"){
        get{
            val pets = petsRepository.getPets()
            call.respond(pets)
        }

        get("/inadoption"){
            val petsInAdoption = petsRepository.getPetsInAdoption()
            call.respond(petsInAdoption)
        }

        post {
            val newPet = call.receive<Pet>()

            petsRepository.insertPet(
                userId = newPet.userId,
                inAdoption = newPet.inAdoption,
                name = newPet.name,
                type = newPet.type,
                gender = newPet.gender,
                breed = newPet.breed,
                castrated = newPet.castrated,
                medHistId = newPet.medHistId,
                imgId = newPet.imgId
            )
            call.respondText("S'ha registrat correctament")
        }
    }
}