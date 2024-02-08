package com.petlink.routes

import com.petlink.database.repositories.PetAdditionalInfoRepository
import com.petlink.models.Pet
import com.petlink.models.PetAdditionalInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val petAdditionalInfoRepository = PetAdditionalInfoRepository()

fun Route.petsAdditionalInfoRouting(){
    route("/additionalInfoPet"){
        get("/{petId?}") {
            val id = call.parameters["petId"]
            if (id.isNullOrBlank()){
                return@get call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            }

            val petInfo = petAdditionalInfoRepository.getAllInfoByPetId(id.toInt())
            call.respond(petInfo!!)
        }

        post {
            val newPetInfo = call.receive<PetAdditionalInfo>()

            petAdditionalInfoRepository.insertPetInfoAdditional(
                petId = newPetInfo.petId,
                age = newPetInfo.age,
                weight = newPetInfo.weight,
                size = newPetInfo.size,
                description = newPetInfo.description
            )
            call.respondText("Se ha registrado correctamente la info addicional de la mascota")
        }

    }
}