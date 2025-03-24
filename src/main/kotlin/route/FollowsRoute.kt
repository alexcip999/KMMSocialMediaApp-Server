package com.example.route

import com.example.model.FollowAndUnfollowResponse
import com.example.model.FollowsParams
import com.example.repository.follows.FollowsRepository
import com.example.util.Constants
import com.example.util.getLongParameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Routing.followRouting(
    repository: FollowsRepository
){
    authenticate {

        route(path = "/follows") {
            post(path = "/follow") {
                try {
                    val params = call.receiveNullable<FollowsParams>()

                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = FollowAndUnfollowResponse(
                                success = false,
                                message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                            )
                        )
                        return@post
                    }

                    val result = repository.followUser(follower = params.follower, following = params.following)

                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = FollowAndUnfollowResponse(
                            success = false,
                            message = Constants.UNEXPECTED_ERROR_MESSAGE
                        )
                    )
                }
            }

            post("/unfollow") {
                try {
                    val params = call.receiveNullable<FollowsParams>()
                    if (params == null) {
                        call.respond(
                            status = HttpStatusCode.BadRequest,
                            message = FollowAndUnfollowResponse(
                                success = false,
                                message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                            )
                        )
                        return@post
                    }

                    val result = repository.unfollowUser(follower = params.follower, following = params.following)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = Constants.UNEXPECTED_ERROR_MESSAGE
                    )
                }
            }

            get("/followers") {
                try {
                    val userId = call.getLongParameter(name = Constants.USER_ID_PARAMETER, isQueryParameter = true)
                    val page = call.request.queryParameters[Constants.USER_ID_PARAMETER]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters[Constants.PAGE_LIMIT_PARAMETER]?.toIntOrNull()
                        ?: Constants.DEFAULT_PAGE_SIZE

                    val result = repository.getFollowers(userId = userId, pageNumber = page, pageSize = limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = Constants.UNEXPECTED_ERROR_MESSAGE
                    )
                }
            }

            get("/following") {
                try {
                    val userId = call.getLongParameter(name = Constants.USER_ID_PARAMETER, isQueryParameter = true)
                    val page = call.request.queryParameters[Constants.PAGE_NUMBER_PARAMETER]?.toIntOrNull() ?: 0
                    val limit = call.request.queryParameters[Constants.PAGE_LIMIT_PARAMETER]?.toIntOrNull()
                        ?: Constants.DEFAULT_PAGE_SIZE

                    val result = repository.getFollowing(userId = userId, pageNumber = page, pageSize = limit)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                    )
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = Constants.UNEXPECTED_ERROR_MESSAGE
                    )
                }
            }

            get("/suggestions") {
                try {
                    val userId = call.getLongParameter(name = Constants.USER_ID_PARAMETER, isQueryParameter = true)
                    val result = repository.getFollowingSuggestions(userId = userId)
                    call.respond(
                        status = result.code,
                        message = result.data
                    )
                } catch (badRequestError: BadRequestException) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = Constants.MISSING_PARAMETERS_ERROR_MESSAGE
                    )
                } catch (anyError: Throwable) {
                    call.application.log.error("Unexpected error: ${anyError.message}", anyError)
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = Constants.UNEXPECTED_ERROR_MESSAGE
                    )
                }
            }
        }
    }
}