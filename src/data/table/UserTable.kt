package com.example.data.table

import org.jetbrains.exposed.sql.Table


// Create User table
object UserTable : Table() {

    val username = varchar("username",50)
    val email = varchar("email",50)
    val password = varchar("password",50)

    override val primaryKey: PrimaryKey = PrimaryKey(email)

}