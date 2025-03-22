package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.dao.user.UserDao
import com.example.model.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

private const val CLAIM = "email"
private const val jwtAudience = "users"
private const val jwtDomain = "http://127.0.0.1"
private const val jwtSecret = "myjwtsecret"

fun Application.configureSecurity() {

    val userDao by inject<UserDao>()

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
                    val userExists = userDao.findByEmail(email = credential.payload.getClaim(CLAIM).asString()) != null
                    val isValidAudiennce = credential.payload.audience.contains(jwtAudience)
                    if (userExists && isValidAudiennce){
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
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
