package com.example.repository.auth.follows

import com.example.dao.follows.FollowsDao
import com.example.dao.user.UserDao
import com.example.model.FollowAndUnfollowResponse
import com.example.repository.follows.FollowsRepository
import com.example.util.Response
import io.ktor.http.*

class FollowsRepositoryImpl(
    private val userDao: UserDao,
    private val followDao: FollowsDao
) : FollowsRepository {
    override suspend fun followUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        return if (followDao.isAllreadyFollowing(follower, following)) {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "You are allready following this user!"
                )
            )
        }else{
            val success = followDao.followUser(follower, following)

            if (success){
                userDao.updateFollowsCount(follower, following, isFollowing = true)
                Response.Success(
                    data = FollowAndUnfollowResponse(
                        success = true
                    )
                )
            }else{
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = FollowAndUnfollowResponse(
                        success = false,
                        message = "Ooops, something went wrong on our side, please try again!"
                    )
                )
            }
        }
    }

    override suspend fun unfollowUser(follower: Long, following: Long): Response<FollowAndUnfollowResponse> {
        val success = followDao.unfollowUser(follower, following)

        return if (success){
            userDao.updateFollowsCount(follower, following, isFollowing = true)
            Response.Success(
                data = FollowAndUnfollowResponse(
                    success = true
                )
            )
        }else{
            Response.Error(
                code = HttpStatusCode.InternalServerError,
                data = FollowAndUnfollowResponse(
                    success = false,
                    message = "Ooops, something went wrong on our side, please try again!"
                )
            )
        }
    }
}