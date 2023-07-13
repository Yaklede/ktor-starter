package com.example.login

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*

fun Application.configureLogin() {

    install(Authentication) {
        session<UserSession>("auth-session") {
            validate { session ->
                if (session.name.startsWith("test")) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }

        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (credentials.name == "test" && credentials.password == "password") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    install(Sessions) {
        cookie<UserSession>("jet_user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
        }
    }

    routing {
        get("/login") {
            call.respondHtml {
                body {
                    form(
                        action = "/login",
                        encType = FormEncType.applicationXWwwFormUrlEncoded,
                        method = FormMethod.post
                    ) {
                        p {
                            +"Username:"
                            textInput(name = "username")
                        }
                        p {
                            +"Password:"
                            passwordInput(name = "password")
                        }
                        p {
                            submitInput() { value = "Login" }
                        }
                    }
                }
            }
        }

        post("/login") {
            val userName = call.principal<UserIdPrincipal>()?.name.toString()
            call.sessions.set(UserSession(name = userName, count = 1))
            call.respondRedirect("/hello")
        }

        authenticate("auth-session") {
            get("/hello") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respond("Hello, ${userSession?.name}! Visit count is ${userSession?.count}.")
            }
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
    }
}

data class UserSession(val name: String, val count: Int) : Principal