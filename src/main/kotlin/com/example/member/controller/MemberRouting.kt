package com.example.member.controller

import com.example.member.repository.MemberCondition
import com.example.member.service.MemberService
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


fun Application.memberRouting(
    memberService: MemberService
) {
    routing() {
        get("/members/{id}") {
            call.parameters["id"]?.toLong()?.let {
                val member = memberService.getMemberById(it)
                call.respond(HttpStatusCode.OK, member)
            }
        }

        authenticate("auth-session") {
            post("/members") {
                val receive = call.receive<MemberCondition>()
                val savedId = memberService.save(condition = receive)
                call.respond(HttpStatusCode.OK, savedId)
            }
        }

        delete("/members/{id}") {
            call.parameters["id"]?.toLong()?.let {
                memberService.delete(it)
            }
        }
    }
}