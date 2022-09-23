package com.example.repository

import com.example.data.modal.User
import com.example.data.table.UserTable
import com.example.repository.Database_Factory.dbQuery
import org.jetbrains.exposed.sql.*


class UserRepo {
    // When User Enter in Server
    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { ut ->
                ut[UserTable.username] = user.UserName
                ut[UserTable.email] = user.Email
                ut[UserTable.password] = user.Password
            }
        }
    }

    // When User Exists
    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map {
                rowToUser(it)
            }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }
        return User(
            UserName = row[UserTable.username],
            Email = row[UserTable.email],
            Password = row[UserTable.password]
        )

    }

}

    // ------ Note Table--------


//    suspend fun addNotes(note : Note, email: String)
//    {
//        Database_Factory.dbQuery {
//            NoteTable.insert { ut ->
//                ut[id] = note.id
//                ut[UserEmail] = email
//                ut[noteTitle] = note.noteTitle
//                ut[description] = note.noteDesc
//                ut[noteDate] = note.date
//
//            }
//        }
//    }
//
//    // Get All Notes
//    suspend fun getAllNotes(email: String)  = Database_Factory.dbQuery {
//        NoteTable.select {
//            NoteTable.UserEmail.eq(email)
//        }.mapNotNull {
//            rowToNote(it)
//        }
//    }
//
//    // Update Note
//    suspend fun UpdateNote(note: Note, email: String)
//    {
//        Database_Factory.dbQuery {
//            NoteTable.update(
//                where = {
//                    NoteTable.UserEmail.eq(email) and NoteTable.id.eq(note.id)
//                }
//            ) { ut ->
//                ut[noteTitle] = note.noteTitle
//                ut[description] = note.noteDesc
//                ut[noteDate] = note.date
//            }
//        }
//    }
//
//    // Delete Note
//    suspend fun DeleteNote(id:String,email: String)
//    {
//        Database_Factory.dbQuery {
//            NoteTable.deleteWhere { NoteTable.UserEmail.eq(email) and  NoteTable.id.eq(id) }
//        }
//    }
//
//    // Note Get in Rows
//    private  fun rowToNote(row: ResultRow?) : Note?
//    {
//        if (row == null)
//        {
//            return null
//        }
//
//        return Note(
//            id = row[NoteTable.id],
//            noteTitle = row[NoteTable.noteTitle],
//            noteDesc = row[NoteTable.description],
//            date = row[NoteTable.noteDate]
//        )
//    }




