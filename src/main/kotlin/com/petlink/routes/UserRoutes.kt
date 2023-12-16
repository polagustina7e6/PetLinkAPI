package com.petlink.routes

import com.petlink.database.repositories.PetsRepository
import com.petlink.database.repositories.UsersRepository
import com.petlink.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val usersRepository = UsersRepository()

fun Route.usersRouting(){
    route("/users"){
        get{
            val users = usersRepository.getUsers()
            call.respond(users)
        }
        post {
            val newUser = call.receive<User>()
            usersRepository.insertUser(
                name= newUser.name,
                dni= newUser.dni,
                email = newUser.email,
                password= newUser.password,
                phone= newUser.phone,
                imgprofile= newUser.imgProfile
            )
            call.respondText("S'ha registrat correctament")
        }
        post("/login") {
            val loginRequest = call.receive<UserAuth>()
            if (usersRepository.verifyUserCredentials(loginRequest.email, loginRequest.password)) {
                call.respondText("Inicio de sesión correcto!")
            } else {
                call.respondText("Credenciales incorrectas", status = HttpStatusCode.Unauthorized)
            }
        }
        get("name/{email?}"){
            if (call.parameters["email"].isNullOrBlank()) {
                return@get call.respondText("Missing EMAIL", status = HttpStatusCode.BadRequest)
            }
            val id = call.parameters["email"]!!
            val nameByEmail = usersRepository.getNameByEmail(id)
            call.respond(nameByEmail)
        }

        get("id/{email?}"){
            if (call.parameters["email"].isNullOrBlank()) {
                return@get call.respondText("Missing EMAIL", status = HttpStatusCode.BadRequest)
            }
            val email = call.parameters["email"]!!
            val nameByEmail = usersRepository.getIdByEmail(email)
            call.respond(nameByEmail)
        }

        get("fullUser/{id?}") {
            if (call.parameters["id"].isNullOrBlank()) {
                return@get call.respondText("Missing userId", status = HttpStatusCode.BadRequest)
            }
            val id = call.parameters["id"]!!
            val function = usersRepository.getUserByUserId(id.toInt())!!
            call.respond(User(function.id, function.name, function.dni, function.phone, function.email, function.password, function.imgProfile))
        }

        put("/{id}") {
            val userId = call.parameters["id"]?.toIntOrNull()
            if (userId == null) {
                call.respondText("User ID not found", status = HttpStatusCode.NotFound)
                return@put
            }

            val updatedUser = call.receive<UserUpdateInfo>()
            val success = usersRepository.updateUsers(userId, updatedUser.name, updatedUser.phone, updatedUser.email)

            if (success) {
                call.respondText("User information updated successfully")
            } else {
                call.respondText("Failed to update user information", status = HttpStatusCode.InternalServerError)
            }
        }


    }
}