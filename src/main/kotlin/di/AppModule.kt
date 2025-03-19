package com.example.di

import com.example.dao.follows.FollowsDao
import com.example.dao.follows.FollowsDaoImpl
import com.example.dao.user.UserDao
import com.example.dao.user.UserDaoImpl
import com.example.repository.auth.auth2.AuthRepository
import com.example.repository.auth.auth2.AuthRepositoryImpl
import com.example.repository.follows.FollowsRepositoryImpl
import com.example.repository.follows.FollowsRepository
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
    single<FollowsDao> { FollowsDaoImpl() }
    single<FollowsRepository> { FollowsRepositoryImpl(get(), get()) }
}