package com.example

import com.example.dao.DataBaseFactory
import com.example.dao.follows.FollowsDaoImpl
import com.example.dao.post.PostDaoImpl
import com.example.dao.post_comments.PostCommentsDao
import com.example.dao.post_comments.PostCommentsDaoImpl
import com.example.dao.post_likes.PostLikeDaoImpl
import com.example.dao.user.UserDaoImpl
import com.example.di.configureDI
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.repository.follows.FollowsRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.repository.auth.AuthRepositoryImpl
import com.example.repository.post.PostRepositoryImpl
import com.example.repository.post_comments.PostCommentsRepositoryImpl
import com.example.repository.post_likes.PostLikesRepositoryImpl
import com.example.repository.profile.ProfileRepositoryImpl

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val userDao = UserDaoImpl()
    val userRepository = AuthRepositoryImpl(userDao)
    val followsDao = FollowsDaoImpl()
    val followRepository = FollowsRepositoryImpl(userDao, followsDao)
    val postDao = PostDaoImpl()
    val postLikeDao = PostLikeDaoImpl()
    val postRepository = PostRepositoryImpl(postDao, followsDao, postLikeDao)
    val profileRepository = ProfileRepositoryImpl(userDao, followsDao)
    val postCommentsDao = PostCommentsDaoImpl()
    val postCommentsRepository = PostCommentsRepositoryImpl(postCommentsDao, postDao)
    val postLikesRepository = PostLikesRepositoryImpl(postLikeDao, postDao)
    DataBaseFactory.init()
    configureSerialization()
    configureDI()
    configureSecurity()
    configureRouting(userRepository, followRepository, postRepository, profileRepository, postCommentsRepository, postLikesRepository)
}
