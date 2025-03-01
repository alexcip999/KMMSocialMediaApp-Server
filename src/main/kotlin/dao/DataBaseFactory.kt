package com.example.dao

import com.example.model.UserRow
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DataBaseFactory {
    fun init(){
        Database.connect(createHikariDataSource())
        transaction {
            SchemaUtils.create(UserRow)
        }
    }

    private fun createHikariDataSource(): HikariDataSource {
        val driverClass = "org.postgresql.Driver"
        val jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        val hikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            setJdbcUrl(jdbcUrl)
            username = "postgres"
            password = "12345678"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T? =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}