package com.example.repository.user

import com.example.dao.user.UserDao
import com.example.model.AuthResponse
import com.example.model.AuthResponseData
import com.example.model.SignInParams
import com.example.model.SignUpParams
import com.example.repository.util.Response
import io.ktor.http.*

class UserRepositoryImpl(
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAllreadyExists(params.email)){
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "A user with this email already exists!"
                )
            )
        } else {
            val insertedUser = userDao.insert(params)

            if (insertedUser == null){
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Ooops, sorry we could not register the user, try later!"
                    )
                )
            } else {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = insertedUser.id,
                            name = insertedUser.name,
                            bio = insertedUser.bio,
                            token = "Here is your token"
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        TODO("Not yet implemented")
    }

    private suspend fun userAllreadyExists(email: String): Boolean {
        return userDao.findByEmail(email) != null
    }
}