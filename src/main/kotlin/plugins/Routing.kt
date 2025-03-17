package com.example.plugins

import com.example.repository.user.AuthRepository
import com.example.route.authRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authRepository: AuthRepository
) {

    routing {
        authRouting(authRepository)
    }
}
