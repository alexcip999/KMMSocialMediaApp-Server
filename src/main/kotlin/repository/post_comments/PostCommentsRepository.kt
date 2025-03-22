package com.example.repository.post_comments

import com.example.model.CommentResponse
import com.example.model.GetCommentResponse
import com.example.model.NewCommentParams
import com.example.model.RemoveCommentParams
import com.example.util.Response

interface PostCommentsRepository {
    suspend fun addComment(params: NewCommentParams): Response<CommentResponse>

    suspend fun removeComment(params: RemoveCommentParams): Response<CommentResponse>

    suspend fun getPostComments(postId: Long, pageNumber: Int, pageSize: Int): Response<GetCommentResponse>
}