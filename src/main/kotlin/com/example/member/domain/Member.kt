package com.example.member.domain

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Member : Table(name = "member_info") {
    val id = long("id").autoIncrement()
    val name = varchar("name", 20)
    val email = varchar("email", 50)
    val phone = varchar("phone", 20)

    override val primaryKey = PrimaryKey(id)
    val index = index(customIndexName = "member_idx_name", columns = arrayOf(name), isUnique = false)
}