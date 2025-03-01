package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

private const val CLAIM = "email"
private val jwtAudience = System.getenv("jwtAudience")
private val jwtDomain = System.getenv("jwt.domain")
private val jwtSecret = System.getenv("jwt.secret")

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim(CLAIM).asString() != null){
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = AuthResponse(
                        errorMessage = "Token is not valid or has expired"
                    )
                )
            }
        }
    }
}

fun generateToken(email: String): String {
    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim(CLAIM, email)
        //.withExpiresAt()
        .sign(Algorithm.HMAC256(jwtSecret))
}
