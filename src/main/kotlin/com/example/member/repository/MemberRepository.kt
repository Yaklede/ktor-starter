package com.example.member.repository

import com.example.database.query
import com.example.member.domain.Member
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class MemberCondition(val name: String? = null, val email: String? = null, val phone: String? = null)
@Serializable
data class MemberDto(val name: String, val email: String, val phone: String)

class MemberRepository(private val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(Member)
        }
    }

    suspend fun save(member: MemberCondition): Long {
        return query {
            Member.insert {
                it[name] = member.name ?: ""
                it[email] = member.email ?: ""
                it[phone] = member.phone ?: ""
            }[Member.id]
        }
    }

    suspend fun findById(id: Long): MemberDto? {
        return query {
            Member.select { Member.id eq id }
                .map { MemberDto(it[Member.name], it[Member.email], it[Member.phone]) }
                .singleOrNull()
        }
    }

    suspend fun delete(id: Long) {
        query {
            Member.deleteWhere { Member.id.eq(id)}
        }
    }
}