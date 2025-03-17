package com.example.dao.user

import org.jetbrains.exposed.sql.Table

object UserTable: Table(name = "users") {
    val id = long("user_id").autoIncrement()
    val name = varchar("user_name", 255)
    val email = varchar("user_email", 255).uniqueIndex()
    val bio = varchar("user_bio", 255).default(
        defaultValue = "Hey, what's up? Welcome to my SocialApp page"
    )
    val password = varchar("user_password", 255)
    val imageUrl = text("image_url").nullable()
    val followersCount = integer("followers_count").default(0)
    val followingCount = integer("following_count").default(0)

    override val primaryKey : PrimaryKey
        get() = PrimaryKey(id)
}

data class UserRow(
    val id: Long,
    val name: String,
    val bio: String,
    val imageUrl: String?,
    val password: String,
    val followersCount: Int,
    val followingCount: Int
)