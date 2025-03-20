package com.example

import com.example.dao.DataBaseFactory
import com.example.dao.follows.FollowsDaoImpl
import com.example.dao.post.PostDaoImpl
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
    DataBaseFactory.init()
    configureSerialization()
    configureDI()
    configureSecurity()
    configureRouting(userRepository, followRepository, postRepository)
}
