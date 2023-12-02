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
    }
}