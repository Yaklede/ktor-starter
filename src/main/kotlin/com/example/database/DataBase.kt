package com.example.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

enum class DataBaseType {
    H2;
}

suspend fun <T> query(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

fun getDataSource(type: DataBaseType): Database {
    return when(type) {
        DataBaseType.H2 -> H2DataSource()
    }
}
fun H2DataSource() = Database.connect(
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "sa",
    driver = "org.h2.Driver",
    password = ""
)