package com.petlink.routes

import com.petlink.database.repositories.PetsRepository
import com.petlink.database.repositories.UsersRepository
import com.petlink.models.Pet
import com.petlink.models.User
import com.petlink.models.Users
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
                imgprofile= newUser.imgprofile
            )
            call.respondText("S'ha registrat correctament")
        }
    }
}