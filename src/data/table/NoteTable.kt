package com.example.data.table

import org.jetbrains.exposed.sql.Table

object NoteTable : Table()
{
    val id = varchar("id",50)
    val UserEmail = varchar("Email",50).references(UserTable.email)
    val noteTitle = text("Title")
    val description = text("Description")
    val noteDate = long("date")


    override val primaryKey: PrimaryKey = PrimaryKey(id)
 }
