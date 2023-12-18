package com.petlink.routes

import com.petlink.database.repositories.PetsRepository
import com.petlink.database.repositories.RequestsRepository
import com.petlink.models.AdoptionRequest
import com.petlink.models.Pet
import com.petlink.models.UserAuth
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


val requestsRepository = RequestsRepository()
fun Route.requestsRouting(){
    route("/adoptionrequests"){
        post {
            val request = call.receive<AdoptionRequest>()
            val result = requestsRepository.insertAdoptionRequest(request.requestingUserId, request.petId)
            if (result != null) {
                call.respondText("Adoption request added successfully")
            } else {
                call.respondText("Failed to add adoption request", status = HttpStatusCode.InternalServerError)
            }
        }
        get("/{petId}") {
            val petId = call.parameters["petId"]?.toIntOrNull()
            if (petId == null) {
                call.respondText("Invalid petId", status = HttpStatusCode.BadRequest)
                return@get
            }
            val adoptionRequests = requestsRepository.getAdoptionRequestsForPet(petId)
            call.respond(adoptionRequests)
        }

        get("/exist/{petId}/{userId}") {
            val petId = call.parameters["petId"]?.toIntOrNull()
            val userId = call.parameters["userId"]?.toIntOrNull()

            if (petId == null || userId == null) {
                call.respondText("Invalid petId", status = HttpStatusCode.BadRequest)
                return@get
            }

            val getExistingRequest = requestsRepository.existsAdoptionRequest(petId, userId)
            call.respond(getExistingRequest)
        }

        /*
        get("/{petId}/{username}") {
            val petId = call.parameters["petId"]?.toIntOrNull()
            val username = call.parameters["username"]
            if (petId == null || username.isNullOrBlank()) {
                call.respondText("Invalid petId or username", status = HttpStatusCode.BadRequest)
                return@get
            }

            val requestId = requestsRepository.getAdoptionRequestId(petId, username)
            if (requestId != null) {
                call.respond(mapOf("requestId" to requestId))
            } else {
                call.respondText("No matching adoption request found", status = HttpStatusCode.NotFound)
            }
        }*/

        delete("/{userId}/{petId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()
            val petId = call.parameters["petId"]?.toIntOrNull()
            if (userId == null || petId == null) {
                call.respondText("Invalid requestId", status = HttpStatusCode.BadRequest)
                return@delete
            }

            val deleted = requestsRepository.deleteAdoptionRequest(userId, petId)
            if (deleted) {
                call.respondText("Adoption request deleted successfully")
            } else {
                call.respondText("Failed to delete adoption request", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}