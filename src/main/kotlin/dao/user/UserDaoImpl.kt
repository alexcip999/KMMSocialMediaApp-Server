package com.example.dao.user

import com.example.dao.DataBaseFactory.dbQuery
import com.example.model.SignUpParams
import com.example.security.hashPassword
import com.example.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl : UserDao {
    override suspend fun insert(params: SignUpParams): UserRow? {
        return dbQuery {
            val insertStatement = UserTable.insert {
                it[id] = IdGenerator.generateId()
                it[name] = params.name
                it[email] = params.email
                it[password] = hashPassword(params.password)
            }

            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }

    }

    override suspend fun findByEmail(email: String): UserRow? {
        return dbQuery {
            UserTable.selectAll()
                .where(UserTable.email.eq(email))
                .mapNotNull { rowToUser(it) }
                .singleOrNull()
        }
    }


    private fun rowToUser(row: ResultRow): UserRow {
        return UserRow(
            id = row[UserTable.id],
            name = row[UserTable.name],
            bio = row[UserTable.bio],
            password = row[UserTable.password],
            imageUrl = row[UserTable.imageUrl],
            followersCount = row[UserTable.followersCount],
            followingCount = row[UserTable.followingCount],
        )
    }
}