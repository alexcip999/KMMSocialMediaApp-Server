package com.example.dao.post_likes

import com.example.dao.DataBaseFactory.dbQuery
import com.example.util.IdGenerator
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class PostLikeDaoImpl : PostLikeDao {
    override suspend fun addLike(postId: Long, userId: Long): Boolean {
        return dbQuery {
            val insertStatement = PostLikesTable.insert {
                it[likeId] = IdGenerator.generateId()
                it[PostLikesTable.postId] = postId
                it[PostLikesTable.userId] = userId
            }
            insertStatement.resultedValues?.isNotEmpty() ?: false
        }!!
    }

    override suspend fun removeLike(postId: Long, userId: Long): Boolean {
        return dbQuery {
            PostLikesTable.deleteWhere {
                (PostLikesTable.postId eq postId) and (PostLikesTable.userId eq userId)
            } > 0
        }!!
    }

    override suspend fun isPostLikedByUser(postId: Long, userId: Long): Boolean {
        return dbQuery {
            PostLikesTable
                .selectAll()
                .where { (PostLikesTable.postId eq postId) and (PostLikesTable.userId eq userId)}
                .toList()
                .isNotEmpty()
        }!!
    }
}