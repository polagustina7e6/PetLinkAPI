package com.petlink.routes

        

import com.petlink.database.repositories.PetsRepository
import com.petlink.models.AdoptionRequest
import com.petlink.models.Pet
import io.ktor.http.*
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

        get("{userId?}"){
            if (call.parameters["userId"].isNullOrBlank()) {
                return@get call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            }
            val id = call.parameters["userId"]
            val petsByUserId = petsRepository.getPetsByUserId(id!!.toInt())
            call.respond(petsByUserId)
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
            call.respondText("Se ha registrado correctamente")
        }

        get("/bybreed/{breed}") {
            val breed = call.parameters["breed"]
            if (breed.isNullOrBlank()) {
                return@get call.respondText("Missing breed parameter", status = HttpStatusCode.BadRequest)
            }
            val petsByBreed = petsRepository.getPetsByBreed(breed.lowercase())
            call.respond(petsByBreed)
        }

        get("/inadoption/{userId?}") {
            val userId = call.parameters["userId"]
            if (userId.isNullOrBlank()) {
                return@get call.respondText("Missing id parameter", status = HttpStatusCode.BadRequest)
            }
            val pets = petsRepository.getPetsInAdoptionByUserId(userId.toInt())
            call.respond(pets)
        }

        get("/getLastPetId") {
            val id = petsRepository.getLastPetId()
            call.respond(id!!)
        }

        put("/{petId}/adoption/{newStatus}") {
            val petId = call.parameters["petId"]?.toIntOrNull()
            val newStatus = call.parameters["newStatus"]?.toBoolean()

            if (petId == null || newStatus == null) {
                call.respondText("Invalid parameters", status = HttpStatusCode.BadRequest)
                return@put
            }

            val updatedPet = petsRepository.updateAdoptionStatus(petId, newStatus)

            if (updatedPet != null) {
                call.respond(updatedPet)
            } else {
                call.respondText("Pet not found", status = HttpStatusCode.NotFound)
            }
        }

        put("/{petId}/adoption/{userId}"){
            val petId = call.parameters["petId"]?.toIntOrNull()
            val newUser = call.parameters["userId"]?.toIntOrNull()


            if (petId == null || newUser == null) {
                call.respondText("Invalid parameters", status = HttpStatusCode.BadRequest)
                return@put
            }

            val updatedOwner = petsRepository.updateOwnerPet(petId, newUser)

            print(call.parameters["petId"]!!.toInt())
            print(call.parameters["userId"]!!.toInt())
            call.respond(updatedOwner)
        }

        put("/{petId}/castrated/{castratedStatus}"){
            val petId = call.parameters["petId"]?.toIntOrNull()
            val castratedStatus = call.parameters["castratedStatus"]?.toBoolean()
            if (petId == null || castratedStatus == null) {
                call.respondText("Invalid parameters", status = HttpStatusCode.BadRequest)
                return@put
            }

            val updatedPet = petsRepository.updateCastratedStatus(petId, castratedStatus)
            if (updatedPet != null){
                call.respond(updatedPet)
            } else{
                call.respondText("Pet not found", status = HttpStatusCode.NotFound)
            }
        }

        get("/bytype/{type}"){
            val type = call.parameters["type"]
            if (type.isNullOrBlank()) {
                return@get call.respondText("Missing breed parameter", status = HttpStatusCode.BadRequest)
            }
            val petsByBreed = petsRepository.getPetsByType(type.lowercase())
            call.respond(petsByBreed)

        }

        get("/{petId}/medHistId"){
            val petId = call.parameters["petId"]?.toIntOrNull()
            if (petId != null) {
                val medHistId = petsRepository.getMedFromPet(petId)
                if (medHistId != null) {
                    call.respond(mapOf("medHistId" to medHistId))
                } else {
                    call.respond(HttpStatusCode.NotFound, "Pet not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid petId")
            }
        }






    }
}