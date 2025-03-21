package com.example.repository.profile

import com.example.model.ProfileResponse
import com.example.model.UpdateUserParams
import com.example.util.Response

interface ProfileRepository {
    suspend fun getUserById(userId: Long, currentUserId: Long): Response<ProfileResponse>

    suspend fun updateUser(updateUserParams: UpdateUserParams): Response<ProfileResponse>

}