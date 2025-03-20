package com.example.plugins

import com.example.repository.auth.AuthRepository
import com.example.repository.follows.FollowsRepository
import com.example.repository.post.PostRepository
import com.example.route.authRouting
import com.example.route.followRouting
import com.example.route.postRouting
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authRepository: AuthRepository,
    followRepository: FollowsRepository,
    postRepository: PostRepository
) {

    routing {
        authRouting(authRepository)
        followRouting(followRepository)
        postRouting(postRepository)
        static {
            resources("static")
        }
    }
}
