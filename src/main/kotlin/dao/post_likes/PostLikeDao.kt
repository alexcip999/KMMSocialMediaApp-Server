package com.example.dao.post_likes

import com.example.model.Post

interface PostLikeDao {
    suspend fun addLike(postId: Long, userId: Long): Boolean

    suspend fun removeLike(postId: Long, userId: Long): Boolean

    suspend fun isPostLikedByUser(postId: Long, userId: Long): Boolean
}