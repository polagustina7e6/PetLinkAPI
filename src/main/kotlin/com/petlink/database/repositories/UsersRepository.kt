package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory
import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.UsersDAO
import com.petlink.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

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

    suspend fun getUserByUserId(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }
            .mapNotNull {
                User(
                    id = it[Users.id],
                    name = it[Users.name],
                    dni = it[Users.dni],
                    phone = it[Users.phone],
                    email = it[Users.email],
                    password = it[Users.password],
                    imgProfile = it[Users.imgProfile]
                )
            }
            .singleOrNull()
    }

    // En esta función hay que validar si és correcto el formato del email y del phone.
    override suspend fun updateUsers(userId: Int, newName:String, newPhone:String, newEmail:String): Boolean = dbQuery {
        val updateStatement = Users.update({ Users.id eq userId }) {
            it[Users.name] = newName
            it[Users.phone] = newPhone
            it[Users.email] = newEmail
        }

        updateStatement > 0
    }

    override suspend fun getUsersFromPetIdRequest(petId: Int): List<User> {
        return transaction {
            Users.select { Users.id inSubQuery AdoptionRequests.slice(AdoptionRequests.requestingUserId).select { AdoptionRequests.petId eq petId } }
                .map { resultRowToUser(it) }
        }
    }

    override suspend fun checkIfInputEmailExists(email: String): Boolean {
        return transaction {
            Users.select { Users.email eq email }
                .count() > 0
        }
    }
}