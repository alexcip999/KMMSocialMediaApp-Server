package com.example

import com.example.dao.DataBaseFactory
import com.example.di.configureDI
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DataBaseFactory.init()
    configureSerialization()
    configureDI()
    configureSecurity()
    configureRouting()
}
