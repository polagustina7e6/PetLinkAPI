package com.petlink.routes

import com.petlink.database.repositories.UserAdditionalInfoRepository
import com.petlink.models.UserAdditionalInfo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val userAdditionalInfoRepository = UserAdditionalInfoRepository()

fun Route.userAdditionalInfoRouting() {
    route("/additionalInfoUser") {
        // GET /additionalInfoUser/{userId?}
        get("/{userId?}") {
            val id = call.parameters["userId"]
            if (id.isNullOrBlank()) {
                return@get call.respondText("Missing ID", status = HttpStatusCode.BadRequest)
            }
            val userInfo = userAdditionalInfoRepository.getUserAdditionalInfoByUserId(id.toInt())
            call.respond(userInfo!!)
        }

        // PUT /additionalInfoUser/{userId}
        put("/{userId}") {
            val userId = call.parameters["userId"]?.toIntOrNull()
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                return@put
            }

            val newUserAdditionalInfo = call.receiveOrNull<UserAdditionalInfo>()
            if (newUserAdditionalInfo == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request body")
                return@put
            }

            val updatedUserInfo = userAdditionalInfoRepository.updateUserAdditionalInfoByUserId(
                userId = userId,
                age = newUserAdditionalInfo.age,
                city = newUserAdditionalInfo.city,
                slogan = newUserAdditionalInfo.slogan,
                description = newUserAdditionalInfo.description,
                foster = newUserAdditionalInfo.foster,
                imgId = newUserAdditionalInfo.imgId

            )

            if (updatedUserInfo != null) {
                call.respond(HttpStatusCode.OK, "User additional info updated successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "User additional info not found")
            }
        }
    }
}