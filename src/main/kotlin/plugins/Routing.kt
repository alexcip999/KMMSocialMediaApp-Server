package com.example.plugins

import com.example.repository.user.UserRepository
import com.example.repository.user.UserRepositoryImpl
import com.example.route.authRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authRepository: UserRepository
) {

    routing {
        authRouting(authRepository)
    }
}
