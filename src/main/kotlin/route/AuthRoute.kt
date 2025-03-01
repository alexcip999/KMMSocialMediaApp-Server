package com.example.route

import com.example.model.AuthResponse
import com.example.model.SignUpParams
import com.example.repository.user.UserRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import io.ktor.server.response.*

fun Routing.authRouting() {
    val repository by inject<UserRepository>()

    route("/signup") {
        post {
            val params = call.receiveNullable<SignUpParams>()

            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid Credentials!"
                    )
                )

                return@post
            }

            val result = repository.signUp(params = params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}