package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeParams(
    val postId: Long,
    val userId: Long
)

@Serializable
data class LikesResponse(
    val success: Boolean,
    val message: String? = null
)