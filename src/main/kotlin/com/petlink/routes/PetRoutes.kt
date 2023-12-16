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

            if (updatedOwner != null) {
                call.respond(updatedOwner)
            } else {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
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

    }
}