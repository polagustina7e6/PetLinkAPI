package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory
import com.petlink.database.dao.UsersDAO
import com.petlink.models.Pet
import com.petlink.models.Pets
import com.petlink.models.User
import com.petlink.models.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert

class UsersRepository: UsersDAO {
    private fun resultRowToUser(row: ResultRow) = User(
        id= row[Users.id],
        name= row[Users.name],
        dni= row[Users.dni],
        email = row[Users.email],
        password= row[Users.password],
        phone= row[Users.phone],
        imgprofile= row[Users.imgprofile]
    )
    override suspend fun insertUser(
        name: String,
        dni: String,
        email: String,
        password: String,
        phone: Int,
        imgprofile: String
    ): User? = DatabaseFactory.dbQuery {
        val insertStatement = Users.insert {
            it[Users.name] = name
            it[Users.dni] = dni
            it[Users.email] = email
            it[Users.password] = password
            it[Users.phone] = phone
            it[Users.imgprofile] = imgprofile
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)

    }

    override suspend fun getUsers(): List<User> {
        TODO("Not yet implemented")
    }

}