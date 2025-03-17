package com.example.di

import com.example.dao.user.UserDao
import com.example.dao.user.UserDaoImpl
import com.example.repository.user.AuthRepository
import com.example.repository.user.AuthRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
}