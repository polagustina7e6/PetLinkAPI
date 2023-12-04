package com.petlink.database.repositories

import com.petlink.database.DatabaseFactory
import com.petlink.database.dao.RequestsDAO
import com.petlink.models.AdoptionRequest
import com.petlink.models.AdoptionRequests
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import javax.xml.crypto.Data
import kotlin.math.absoluteValue

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

    override suspend fun getAdoptionRequestId(petId: Int, fullname: String): Int? = DatabaseFactory.dbQuery {
        try {
            AdoptionRequests
                .select { (AdoptionRequests.petId eq petId) and (AdoptionRequests.fullname.lowerCase() eq fullname.lowercase())}
                .singleOrNull()?.get(AdoptionRequests.id)
        } catch (e: Exception) {
            println("Error en la obtención del ID de solicitud de adopción: $e")
            e.printStackTrace()
            null
        }

    }

    override suspend fun deleteAdoptionRequest(requestId: Int): Boolean {
        return transaction {
            AdoptionRequests.deleteWhere { AdoptionRequests.id eq requestId } > 0
        }
    }
    override suspend fun getAdoptionRequestsForPet(petId: Int): List<String> = DatabaseFactory.dbQuery {
        AdoptionRequests.select { AdoptionRequests.petId eq petId }
            .map { it[AdoptionRequests.fullname] }
    }

    }
