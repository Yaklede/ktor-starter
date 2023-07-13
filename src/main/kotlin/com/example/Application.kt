package com.example

import com.example.database.DataBaseType
import com.example.database.getDataSource
import com.example.login.configureLogin
import com.example.member.controller.memberRouting
import com.example.member.repository.MemberRepository
import com.example.member.service.MemberService
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*

fun main() {
    embeddedServer(Netty, port = 8089, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureLogin()
    configureSerialization()
//    configureDatabases()
    configureRouting()
    memberRouting(memberInit())
}
fun memberInit(): MemberService {
    val memberRepository = MemberRepository(getDataSource(DataBaseType.H2))
    return MemberService(memberRepository)
}
