package com.example.di

import com.example.dao.follows.FollowsDao
import com.example.dao.follows.FollowsDaoImpl
import com.example.dao.post.PostDao
import com.example.dao.post.PostDaoImpl
import com.example.dao.post_likes.PostLikeDao
import com.example.dao.post_likes.PostLikeDaoImpl
import com.example.dao.user.UserDao
import com.example.dao.user.UserDaoImpl
import com.example.repository.auth.AuthRepository
import com.example.repository.auth.AuthRepositoryImpl
import com.example.repository.follows.FollowsRepositoryImpl
import com.example.repository.follows.FollowsRepository
import com.example.repository.post.PostRepository
import com.example.repository.post.PostRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
    single<FollowsDao> { FollowsDaoImpl() }
    single<FollowsRepository> { FollowsRepositoryImpl(get(), get()) }
    single<PostLikeDao> { PostLikeDaoImpl() }
    single<PostDao> { PostDaoImpl() }
    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }

}