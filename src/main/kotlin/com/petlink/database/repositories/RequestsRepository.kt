package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory
import com.petlink.database.DatabaseFactory.dbQuery
import com.petlink.database.dao.RequestsDAO
import com.petlink.models.AdoptionRequest
import com.petlink.models.AdoptionRequests
import com.petlink.models.Pets
import com.petlink.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import javax.xml.crypto.Data
import kotlin.math.absoluteValue

class RequestsRepository: RequestsDAO {
    private fun resultRowToAdoptionRequest(row: ResultRow) = AdoptionRequest(
        id = row[AdoptionRequests.id],
        requestingUserId = row[AdoptionRequests.requestingUserId],
        petId = row[AdoptionRequests.petId]
    )

    override suspend fun insertAdoptionRequest(requestingUserId: Int, petId: Int): Boolean {
        DatabaseFactory.dbQuery {
            val insertStatement = AdoptionRequests.insert {
                it[AdoptionRequests.requestingUserId] = requestingUserId
                it[AdoptionRequests.petId] = petId
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAdoptionRequest)
        }
        return true
    }

    override suspend fun deleteAdoptionRequest(requestingUserId: Int, petId: Int): Boolean {
        return transaction {
            AdoptionRequests.deleteWhere { AdoptionRequests.requestingUserId eq requestingUserId and(AdoptionRequests.petId eq petId) } > 0
        }
    }

    override suspend fun getAdoptionRequestsForPet(petId: Int) : List<AdoptionRequest> = dbQuery {
        AdoptionRequests.select { AdoptionRequests.petId eq petId }.map(::resultRowToAdoptionRequest)
    }

    override suspend fun existsAdoptionRequest(petId: Int, requestingUserId: Int): Boolean {
         return transaction {
             AdoptionRequests.select { AdoptionRequests.petId eq petId and(AdoptionRequests.requestingUserId eq requestingUserId) }.count() > 0
         }
    }

}
