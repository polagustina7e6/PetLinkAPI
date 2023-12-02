package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory
import com.petlink.database.dao.RequestsDAO
import com.petlink.models.AdoptionRequest
import com.petlink.models.AdoptionRequests
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class RequestsRepository: RequestsDAO {
    private fun resultRowToAdoptionRequest(row: ResultRow) = AdoptionRequest(
        id = row[AdoptionRequests.id],
        fullname = row[AdoptionRequests.fullname],
        petId = row[AdoptionRequests.petId]
    )


    override suspend fun insertAdoptionRequest(fullname: String, petId: Int): AdoptionRequest? =
        DatabaseFactory.dbQuery {
            val insertStatement = AdoptionRequests.insert {
                it[AdoptionRequests.fullname] = fullname
                it[AdoptionRequests.petId] = petId
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAdoptionRequest)
        }
    override suspend fun getAdoptionRequestsForPet(petId: Int): List<String> = DatabaseFactory.dbQuery {
        AdoptionRequests.select { AdoptionRequests.petId eq petId }
            .map { it[AdoptionRequests.fullname] }
    }

}