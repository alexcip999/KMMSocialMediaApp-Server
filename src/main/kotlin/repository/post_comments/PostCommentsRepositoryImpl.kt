package com.example.repository.post_comments

import com.example.dao.post.PostDao
import com.example.dao.post_comments.PostCommentRow
import com.example.dao.post_comments.PostCommentsDao
import com.example.model.*
import com.example.util.Response
import io.ktor.http.*

class PostCommentsRepositoryImpl(
    private val commentsDao: PostCommentsDao,
    private val postDao: PostDao
) : PostCommentsRepository {
    override suspend fun addComment(params: NewCommentParams): Response<CommentResponse> {
        val postCommentRow = commentsDao.addComment(
            params.postId,
            params.userId,
            params.content
        )

        return if (postCommentRow == null){
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = CommentResponse(
                    success = false,
                    message = "Could not insert comment into the db"
                )
            )
        } else {
            postDao.updateCommentsCount(postId = params.postId)
            Response.Success(
                data = CommentResponse(success = false, comment = toPostCommnet(postCommentRow))
            )
        }
    }

    override suspend fun removeComment(params: RemoveCommentParams): Response<CommentResponse> {
        val commnetRow = commentsDao.findComment(commentId = params.commentId, postId = params.postId)

        return if (commnetRow == null){
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = CommentResponse(success = false, message = "Comment ${params.commentId} not found")
            )
        } else {
            val postOwnerId = postDao.getPost(postId = params.postId)!!.userId

            if (params.userId != commnetRow.userId && params.userId != postOwnerId){
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = CommentResponse(
                        success = false,
                        message = "User ${params.userId} cannot delete commnet ${params.commentId}"
                    )
                )
            } else {
                val commentWasRemoved = commentsDao.removeComment(commentId = params.commentId, postId = params.postId)

                if (commentWasRemoved){
                    postDao.updateCommentsCount(postId = params.postId, decrement = false)
                    Response.Success(
                        data = CommentResponse(success = true)
                    )
                } else {
                    Response.Error(
                        code = HttpStatusCode.Conflict,
                        data = CommentResponse(
                            success = false,
                            message = "Could not remove comment ${params.commentId}"
                        )
                    )
                }
            }
        }
    }

    override suspend fun getPostComments(postId: Long, pageNumber: Int, pageSize: Int): Response<GetCommentResponse> {
        val commentRows = commentsDao.getComments(postId = postId, pageNumber = pageNumber, pageSize = pageSize)
        val comments = commentRows.map {
            toPostCommnet(it)
        }

        return Response.Success(
            data = GetCommentResponse(success = true, comments = comments)
        )
    }

    private fun toPostCommnet(commentRow: PostCommentRow): PostComment {
        return PostComment(
            commentId = commentRow.commentId,
            content = commentRow.content,
            postId = commentRow.postId,
            userId = commentRow.userId,
            userName = commentRow.userName,
            userImageUrl = commentRow.userImageUrl,
            createdAt = commentRow.createdAt
        )
    }
}