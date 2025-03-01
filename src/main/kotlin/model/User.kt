package com.example.model

import org.jetbrains.exposed.sql.Table

object UserRow: Table(name = "users") {
    val id = integer("user_id").autoIncrement()
    val name = varchar("user_name", 255)
    val email = varchar("user_email", 255)
    val bio = varchar("user_bio", 255).default(
        defaultValue = "Hey, what's up? Welcome to my SocialApp page"
    )
    val password = varchar("user_password", 255)
    val avatar = text("user_avatar").nullable()

    override val primaryKey : PrimaryKey?
        get() = PrimaryKey(id)
}

data class User(
    val id: Int,
    val name: String,
    val bio: String,
    val avatar: String?,
    val password: String
)