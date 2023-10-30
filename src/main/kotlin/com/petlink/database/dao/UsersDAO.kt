package com.petlink.database.dao

import com.petlink.models.Pet
import com.petlink.models.User

interface UsersDAO {
    suspend fun insertUser(name:String, dni:String, email:String, password:String, phone:Int, imgprofile:String): User?
    suspend fun getUsers(): List<User>
}