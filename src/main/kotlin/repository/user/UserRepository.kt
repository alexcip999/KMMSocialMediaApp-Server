package com.example.repository.user

import com.example.model.AuthResponse
import com.example.model.SignInParams
import com.example.model.SignUpParams
import com.example.repository.util.Response

interface UserRepository {
    suspend fun signUp(params: SignUpParams) : Response<AuthResponse>
    suspend fun signIn(params: SignInParams) : Response<AuthResponse>
}