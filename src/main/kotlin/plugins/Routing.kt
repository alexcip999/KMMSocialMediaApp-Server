package com.example.plugins

import com.example.repository.auth.AuthRepository
import com.example.repository.follows.FollowsRepository
import com.example.repository.post.PostRepository
import com.example.repository.profile.ProfileRepository
import com.example.route.authRouting
import com.example.route.followRouting
import com.example.route.postRouting
import com.example.route.profileRouting
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authRepository: AuthRepository,
    followRepository: FollowsRepository,
    postRepository: PostRepository,
    profileRepository: ProfileRepository
) {

    routing {
        authRouting(authRepository)
        followRouting(followRepository)
        postRouting(postRepository)
        profileRouting(profileRepository)
        static {
            resources("static")
        }
    }
}
