package com.example.repository.post_likes

import com.example.model.LikeParams
import com.example.model.LikesResponse
import com.example.util.Response

interface PostLikesRepository {
    suspend fun addLike(params: LikeParams): Response<LikesResponse>

    suspend fun removeLike(params: LikeParams): Response<LikesResponse>
}