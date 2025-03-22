package com.example.route

import com.example.model.ProfileResponse
import com.example.model.UpdateUserParams
import com.example.repository.profile.ProfileRepository
import com.example.util.Constants
import com.example.util.getLongParameter
import com.example.util.saveFile
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import java.io.File

fun Routing.profileRouting(
    repository: ProfileRepository
) {
    authenticate {

        route("/profile") {

            get("/{userId}") {
                try {
                    val profileOwnerId = call.getLongParameter(name = "userId")
                    val currentUserId = call.getLongParameter(name = "currentUserId", isQueryParameter = true)

                    val result = repository.getUserById(profileOwnerId, currentUserId)
                    call.respond(status = result.code, message = result.data)
                } catch (badRequestError: BadRequestException) {
                    return@get
                } catch (anyError: Throwable) {
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = ProfileResponse(
                            success = false,
                            message = "An unexpected error has occured, try again!"
                        )
                    )
                }
            }

            post("/update") {
                var fileName = ""
                var updateUserParams: UpdateUserParams? = null
                val multiPartData = call.receiveMultipart()

                try {
                    multiPartData.forEachPart { partData ->
                        when (partData) {
                            is PartData.FileItem -> {
                                fileName = partData.saveFile(folderPath = Constants.PROFILE_IMAGES_FOLDER_PATH)
                            }

                            is PartData.FormItem -> {
                                if (partData.name == "profile_data") {
                                    updateUserParams = Json.decodeFromString(partData.value)
                                }
                            }

                            else -> {}
                        }
                        partData.dispose()
                    }

                    val imageUrl = "${Constants.BASE_URL}${Constants.PROFILE_IMAGES_FOLDER}$fileName"

                    val result = repository.updateUser(
                        updateUserParams = updateUserParams!!.copy(
                            imageUrl = if (fileName.isNotEmpty()) imageUrl else updateUserParams!!.imageUrl
                        )
                    )

                    call.respond(status = result.code, message = result.data)

                } catch (anyError: Throwable) {
                    if (fileName.isNotEmpty()) {
                        File("${Constants.PROFILE_IMAGES_FOLDER_PATH}/$fileName").delete()
                    }
                    call.respond(
                        status = HttpStatusCode.InternalServerError,
                        message = ProfileResponse(
                            success = false,
                            message = "An unexpected error has occured, try again!"
                        )
                    )
                }
            }
        }
    }
}