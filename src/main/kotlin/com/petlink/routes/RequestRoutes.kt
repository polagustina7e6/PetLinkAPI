package com.petlink.routes

import com.petlink.database.repositories.PetsRepository
import com.petlink.database.repositories.RequestsRepository
import com.petlink.models.AdoptionRequest
import com.petlink.models.Pet
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
            val result = requestsRepository.insertAdoptionRequest(request.fullname, request.petId)
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
        }

        delete("/{requestId}") {
            val requestId = call.parameters["requestId"]?.toIntOrNull()
            if (requestId == null) {
                call.respondText("Invalid requestId", status = HttpStatusCode.BadRequest)
                return@delete
            }

            val deleted = requestsRepository.deleteAdoptionRequest(requestId)
            if (deleted) {
                call.respondText("Adoption request deleted successfully")
            } else {
                call.respondText("Failed to delete adoption request", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}