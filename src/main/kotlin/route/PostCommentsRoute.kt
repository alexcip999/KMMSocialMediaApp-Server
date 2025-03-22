package com.example.route

import com.example.model.CommentResponse
import com.example.model.NewCommentParams
import com.example.model.RemoveCommentParams
import com.example.repository.post_comments.PostCommentsRepository
import com.example.util.Constants
import com.example.util.getLongParameter
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Routing.postCommentsRouting(
    repository: PostCommentsRepository
){
    route("/post/comments"){

        post("/create"){
            try {
                val params = call.receiveNullable<NewCommentParams>()

                if (params == null){
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = CommentResponse(
                            success = false,
                            message = "Could not parse comment parameters"
                        )
                    )
                    return@post
                }

                val result = repository.addComment(params)
                call.respond(status = result.code, message = result.data)
            }catch (anyError: Throwable){
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = CommentResponse(
                        success = false,
                        message = "An unexpected error occurred, try again!"
                    )
                )
            }
        }

        delete("/delete"){
            try {
                val params = call.receiveNullable<RemoveCommentParams>()

                if (params == null){
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = CommentResponse(
                            success = false,
                            message = "Could not parse comment parameters"
                        )
                    )
                    return@delete
                }

                val result = repository.removeComment(params)
                call.respond(status = result.code, message = result.data)
            }catch (anyError: Throwable){
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = CommentResponse(
                        success = false,
                        message = "An unexpected error occurred, try again!"
                    )
                )
            }
        }

        get("/{postId}"){
            try {
                val postId = call.getLongParameter(name = "postId")
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_SIZE

                val result = repository.getPostComments(postId = postId, pageNumber = page, pageSize = limit)
                call.respond(status = result.code, message = result.data)
            }catch (anyError: Throwable){
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = CommentResponse(
                        success = false,
                        message = "An unexpected error occurred, try again!"
                    )
                )
            }
        }
    }
}