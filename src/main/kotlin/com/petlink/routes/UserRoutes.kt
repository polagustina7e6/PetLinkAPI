package com.petlink.routes

import com.petlink.database.repositories.UsersRepository
import com.petlink.models.User
import com.petlink.models.UserAuth
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
                phone= newUser.phone,
                email = newUser.email,
                password = newUser.password,
                imgprofile = newUser.imgProfile
            )
            call.respondText("S'ha registrat correctament")
        }
        post("/login") {
            val loginRequest = call.receive<UserAuth>()

            if (loginRequest.email.isNotBlank() && loginRequest.password.isNotBlank()) {
                if (usersRepository.verifyUserCredentials(loginRequest.email, loginRequest.password)) {
                    call.respondText("Inicio de sesión correcto!")
                } else {
                    call.respondText("Credenciales incorrectas", status = HttpStatusCode.Unauthorized)
                }
            } else {
                call.respondText("Datos de inicio de sesión incorrectos", status = HttpStatusCode.BadRequest)
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
    }
}