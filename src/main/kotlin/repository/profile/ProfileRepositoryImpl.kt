package com.example.repository.profile

import com.example.dao.follows.FollowsDao
import com.example.dao.user.UserDao
import com.example.dao.user.UserRow
import com.example.model.Profile
import com.example.model.ProfileResponse
import com.example.model.UpdateUserParams
import com.example.util.Response
import io.ktor.http.*

class ProfileRepositoryImpl(
    private val userDao: UserDao,
    private val followsDao: FollowsDao
) : ProfileRepository {
    override suspend fun getUserById(userId: Long, currentUserId: Long): Response<ProfileResponse> {
        val userRow = userDao.findById(userId = userId)

        return if (userRow == null){
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = ProfileResponse(
                    success = false,
                    message = "Could not find user with id: $userId"
                )
            )
        } else {
            val isFollowing = followsDao.isAllreadyFollowing(follower = currentUserId, following = userId)
            val isOwnProfile = userId == currentUserId

            Response.Success(
                data = ProfileResponse(
                    success = true,
                    profile = toProfile(userRow, isFollowing, isOwnProfile)
                )
            )
        }
    }

    override suspend fun updateUser(updateUserParams: UpdateUserParams): Response<ProfileResponse> {
        val userExists = userDao.findById(userId = updateUserParams.userId) != null

        if (userExists){
            val userUpdated =
                userDao.updateUser(
                    userId = updateUserParams.userId,
                    name = updateUserParams.name,
                    bio = updateUserParams.bio,
                    imageUrl = updateUserParams.imageUrl
                )


            return if (userUpdated == true){
                Response.Success(
                    data = ProfileResponse(success = true)
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Conflict,
                    data = ProfileResponse(
                        success = false,
                        message = "Could not update user with id: ${updateUserParams.userId}"
                    )
                )
            }
        } else {
            return Response.Error(
                code = HttpStatusCode.NotFound,
                data = ProfileResponse(
                    success = false,
                    message = "Could not find user: ${updateUserParams.userId}"
                )
            )

        }


    }

    private fun toProfile(userRow: UserRow, isFollowing: Boolean, isOwnProfile: Boolean): Profile {
        return Profile(
            id = userRow.id,
            name = userRow.name,
            bio = userRow.bio,
            imageUrl = userRow.imageUrl,
            followersCount = userRow.followersCount,
            followingCount = userRow.followingCount,
            isFollowing = isFollowing,
            isOwnProfile = isOwnProfile
        )
    }
}