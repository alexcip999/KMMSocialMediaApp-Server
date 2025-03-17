package com.example.route

import com.example.model.AuthResponse
import com.example.model.SignInParams
import com.example.model.SignUpParams
import com.example.repository.user.AuthRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Routing.authRouting(
    repository: AuthRepository,
) {

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

    route("/login") {
        post {
            val params = call.receiveNullable<SignInParams>()

            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid Credentials!"
                    )
                )

                return@post
            }

            val result = repository.signIn(params = params)
            call.respond(
                status = result.code,
                message = result.data
            )
        }
    }
}