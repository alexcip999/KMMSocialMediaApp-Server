package com.example.repository.post

import com.example.model.PostResponse
import com.example.model.PostTextParams
import com.example.model.PostsResponse
import com.example.util.Response

interface PostRepository {
    suspend fun createPost(imageUrl: String,postTextParams: PostTextParams): Response<PostResponse>

    suspend fun getFeedPosts(userId: Long, pageNumber: Int, pageSize: Int): Response<PostsResponse>

    suspend fun getPostsByUser(
        postsOwnerId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int,
    ): Response<PostsResponse>

    suspend fun getPost(postId: Long, currentUserId: Long): Response<PostResponse>

    suspend fun deletePost(postId: Long): Response<PostResponse>
}