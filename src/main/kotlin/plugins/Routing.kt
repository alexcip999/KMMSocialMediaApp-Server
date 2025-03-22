package com.example.plugins

import com.example.repository.auth.AuthRepository
import com.example.repository.follows.FollowsRepository
import com.example.repository.post.PostRepository
import com.example.repository.post_comments.PostCommentsRepository
import com.example.repository.post_likes.PostLikesRepository
import com.example.repository.profile.ProfileRepository
import com.example.route.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authRepository: AuthRepository,
    followRepository: FollowsRepository,
    postRepository: PostRepository,
    profileRepository: ProfileRepository,
    postCommentsRepository: PostCommentsRepository,
    postLikesRepository: PostLikesRepository
) {

    routing {
        authRouting(authRepository)
        followRouting(followRepository)
        postRouting(postRepository)
        profileRouting(profileRepository)
        postCommentsRouting(postCommentsRepository)
        postLikesRouting(postLikesRepository)
        static {
            resources("static")
        }
    }
}
