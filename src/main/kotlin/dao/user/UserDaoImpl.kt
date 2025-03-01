package com.example.dao.user

import com.example.dao.DataBaseFactory.dbQuery
import com.example.model.SignUpParams
import com.example.model.User
import com.example.model.UserRow
import com.example.security.hashPassword
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoImpl : UserDao {
    override suspend fun insert(params: SignUpParams): User? {
        return dbQuery {
            val insertStatement = UserRow.insert {
                it[name] = params.name
                it[email] = params.email
                it[password] = hashPassword(params.password)
            }

            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }

    }

    override suspend fun findByEmail(email: String): User? {
        return dbQuery {
            UserRow.selectAll()
                .where(UserRow.email.eq(email))
                .mapNotNull { rowToUser(it) }
                .singleOrNull()
        }
    }


    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[UserRow.id],
            name = row[UserRow.name],
            bio = row[UserRow.bio],
            avatar = row[UserRow.avatar],
            password = row[UserRow.password],
        )
    }
}