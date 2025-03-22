package com.example.repository.post_likes

import com.example.dao.post.PostDao
import com.example.dao.post_likes.PostLikeDao
import com.example.model.LikeParams
import com.example.model.LikesResponse
import com.example.util.Response
import io.ktor.http.*

class PostLikesRepositoryImpl(
    private val likesDao: PostLikeDao,
    private val postDao: PostDao
) : PostLikesRepository {
    override suspend fun addLike(params: LikeParams): Response<LikesResponse> {
        val likeExists = likesDao.isPostLikedByUser(postId = params.postId, userId = params.userId)
        return if (likeExists){
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = LikesResponse(success = false, message = "Post already liked")
            )
        } else {
            val postLiked = likesDao.addLike(postId = params.postId, userId = params.userId)
            if (postLiked){
                postDao.updateLikesCount(postId = params.postId)
                Response.Success(
                    data = LikesResponse(success = true)
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = LikesResponse(success = false, message = "Unexpected DB error, try again!")
                )
            }
        }
    }

    override suspend fun removeLike(params: LikeParams): Response<LikesResponse> {
        val likeExists = likesDao.isPostLikedByUser(postId = params.postId, userId = params.userId)
        return if (likeExists){
            val likeRemoved = likesDao.removeLike(postId = params.postId, userId = params.userId)
            if (likeRemoved){
                postDao.updateLikesCount(postId = params.postId, decrement = true)
                Response.Success(
                    data = LikesResponse(success = true)
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = LikesResponse(success = false, message = "Unexpected DB error, try again!")
                )
            }
        } else {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = LikesResponse(success = false, message = "Like not found (may be removed already)")
            )
        }
    }
}