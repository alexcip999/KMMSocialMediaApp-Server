package com.example.repository.post

import com.example.model.PostResponse
import com.example.model.PostTextParams
import com.example.model.PostsResponse
import com.example.util.Response

class PostRepositoryImpl : PostRepository {
    override suspend fun createPost(imageUrl: String, postTextParams: PostTextParams): Response<PostResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFeedPosts(userId: Long, pageNumber: Int, pageSize: Int): Response<PostsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostByUser(
        postsOwnerId: Long,
        currentUserId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Response<PostsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPost(postId: Long, currentUserId: Long): Response<PostResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deletePost(postId: Long): Response<PostResponse> {
        TODO("Not yet implemented")
    }
}