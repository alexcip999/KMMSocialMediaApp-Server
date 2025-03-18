package com.example.plugins

import com.example.repository.auth.AuthRepository
import com.example.repository.follows.FollowsRepository
import com.example.route.authRouting
import com.example.route.followRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authRepository: AuthRepository,
    followRepository: FollowsRepository
) {

    routing {
        authRouting(authRepository)
        followRouting(followRepository)
    }
}
