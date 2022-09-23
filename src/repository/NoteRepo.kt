package com.example.repository

import com.example.data.modal.Note
import com.example.data.table.NoteTable
import org.jetbrains.exposed.sql.*


class NoteRepo
{

    // ------ Note Table--------
    suspend fun addNotes(note : Note, email: String)
    {
        Database_Factory.dbQuery {
            NoteTable.insert { ut ->
                ut[id] = note.id
                ut[UserEmail] = email
                ut[noteTitle] = note.noteTitle
                ut[description] = note.noteDesc
                ut[noteDate] = note.date

            }
        }
    }

    // Get All Notes
    suspend fun getAllNotes(email: String)  = Database_Factory.dbQuery {
        NoteTable.select {
            NoteTable.UserEmail.eq(email)
        }.mapNotNull {
            rowToNote(it)
        }
    }

    // Update Note
    suspend fun UpdateNote(note: Note, email: String)
    {
        Database_Factory.dbQuery {
            NoteTable.update(
                where = {
                    NoteTable.UserEmail.eq(email) and NoteTable.id.eq(note.id)
                }
            ) { ut ->
                ut[noteTitle] = note.noteTitle
                ut[description] = note.noteDesc
                ut[noteDate] = note.date
            }
        }
    }

    // Delete Note
    suspend fun DeleteNote(id:String,email: String)
    {
        Database_Factory.dbQuery {
            NoteTable.deleteWhere { NoteTable.UserEmail.eq(email) and  NoteTable.id.eq(id) }
        }
    }

    // Note Get in Rows
    private  fun rowToNote(row: ResultRow?) : Note?
    {
        if (row == null)
        {
            return null
        }

        return Note(
            id = row[NoteTable.id],
            noteTitle = row[NoteTable.noteTitle],
            noteDesc = row[NoteTable.description],
            date = row[NoteTable.noteDate]
        )
    }

}