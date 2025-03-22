package com.example.route

import com.example.model.LikeParams
import com.example.model.LikesResponse
import com.example.repository.post_likes.PostLikesRepository
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Routing.postLikesRouting(
    repository: PostLikesRepository
){
    route("post/likes"){
        post("/add"){
            try {
                val params = call.receiveNullable<LikeParams>()
                if (params == null){
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = LikesResponse(
                            success = false,
                            message = "Could not parse like parameters"
                        )
                    )
                    return@post
                }

                val result = repository.addLike(params = params)
                call.respond(status = result.code, message = result.data)
            } catch (error: Throwable){
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = LikesResponse(
                        success = false,
                        message = "An unexpected error occurred, try again!"
                    )
                )
            }
        }

        delete("/remove") {
            try {
                val params = call.receiveNullable<LikeParams>()
                if (params == null){
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = LikesResponse(
                            success = false,
                            message = "Could not parse like parameters"
                        )
                    )
                    return@delete
                }

                val result = repository.removeLike(params = params)
                call.respond(status = result.code, message = result.data)
            } catch (error: Throwable){
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = LikesResponse(
                        success = false,
                        message = "An unexpected error occurred, try again!"
                    )
                )
            }
        }
    }
}