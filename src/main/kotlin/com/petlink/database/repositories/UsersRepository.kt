package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory
import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.UsersDAO
import com.petlink.models.User
import com.petlink.models.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class UsersRepository: UsersDAO {
    fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        dni = row[Users.dni],
        phone = row[Users.phone],
        email = row[Users.email],
        password = row[Users.password],
        imgProfile = row[Users.imgProfile]
    )
    override suspend fun insertUser(
        name: String,
        dni: String,
        phone: String,
        email: String,
        password: String,
        imgprofile: String
    ): User? = DatabaseFactory.dbQuery {
        val insertStatement = Users.insert {
            it[Users.name] = name
            it[Users.dni] = dni
            it[Users.phone] = phone
            it[Users.email] = email
            it[Users.password] = password
            it[Users.imgProfile] = imgprofile
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)

    }

    override suspend fun getUsers(): List<User> = dbQuery{
        Users.selectAll().map(::resultRowToUser)
    }

    suspend fun getUserByEmail(email: String): User? = dbQuery {
        Users.select { Users.email eq email }.mapNotNull { resultRowToUser(it) }.singleOrNull()
    }

    suspend fun getPhoneByEmail(email: String): String? = dbQuery {
        Users.select{Users.email eq email}
            .mapNotNull { it[Users.phone] }
            .singleOrNull()
    }

    suspend fun verifyUserCredentials(email: String, password: String): Boolean {
        val user = getUserByEmail(email)
        return user?.password == password
    }

    suspend fun getNameByEmail(email: String): String = dbQuery {
        Users.select { Users.email eq email }
            .mapNotNull { it[Users.name] }
            .singleOrNull() ?: ""
    }

    suspend fun getIdByEmail(email: String): Int = dbQuery {
        Users.select { Users.email eq email }
            .mapNotNull { it[Users.id] }
            .singleOrNull() ?: 0
    }
}