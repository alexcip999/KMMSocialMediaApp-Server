package com.example.dao.follows

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.*

object FollowsTable : Table(name = "follows") {
    val followerId = long("follower_id")
    val followingId = long("following_id")
    val followDate = datetime(name = "follow_date").defaultExpression(CurrentDateTime)
}