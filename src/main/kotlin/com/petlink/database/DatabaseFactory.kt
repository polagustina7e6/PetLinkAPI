package com.petlink.database

import com.petlink.models.Pets
import com.petlink.models.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val user = "avnadmin"
        val password = "AVNS_r9x866LM541U4Cit5EZ"
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:postgresql://pg-petlink-petlink.aivencloud.com:19023/defaultdb?ssl=require&user=avnadmin&password=AVNS_r9x866LM541U4Cit5EZ"
        val database = Database.connect(jdbcURL, driverClassName, user, password)
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(Pets)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T {
        return newSuspendedTransaction(Dispatchers.IO) {
            block()
        }
    }

}

/*
    defaultdb
    pg-petlink-petlink.aivencloud.com
    19023
    avnadmin
    AVNS_r9x866LM541U4Cit5EZ
     */
