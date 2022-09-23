package com.example.route

import com.example.data.modal.Note
import com.example.data.modal.SimpleResponse
import com.example.data.modal.User
import com.example.repository.NoteRepo
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTE = "$NOTES/create"
const val UPDATE_NOTE = "$NOTES/update"
const val DELETE_NOTE = "$NOTES/delete"

@Location(CREATE_NOTE)
class NoteCreateRoute

@Location(NOTES)
class  NoteGetRoute

@Location(UPDATE_NOTE)
class NoteUpdateRoute

@Location(DELETE_NOTE)
class  NoteDeleteRoute



fun Route.NoteRoute(
    db:NoteRepo,
    hashFunction:(String) -> String
)
{
       authenticate("jwt") {

           post<NoteCreateRoute> {
               val note =
                   try {
                       call.receive<Note>()
                   }
                   catch (e : Exception)
                   {
                       call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Some Fields"))
                       return@post
                   }

               try {

                   val email = call.principal<User>()!!.Email
                   db.addNotes(note, email)
                   call.respond(HttpStatusCode.OK,SimpleResponse(true,"Notes Added Successfully"))
               }
               catch (e : Exception)
               {
                   call.respond(HttpStatusCode.Conflict,SimpleResponse(false,"Some Problem Occur"))
               }
           }


           get<NoteGetRoute> {
               try {
                   val email = call.principal<User>()!!.Email
                   val notes = db.getAllNotes(email)
                   call.respond(HttpStatusCode.OK,notes)
               }
               catch (e : Exception)
               {
                   call.respond(HttpStatusCode.Conflict,SimpleResponse(false,"Some Problem Occur"))
               }
           }


           post<NoteUpdateRoute> {
               val note =
                   try {
                       call.receive<Note>()
                   }
                   catch (e : Exception)
                   {
                       call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Some Fields"))
                       return@post
                   }

               try {

                   val email = call.principal<User>()!!.Email
                   db.UpdateNote(note, email)
                   call.respond(HttpStatusCode.OK,SimpleResponse(true,"Notes Update Successfully"))
               }
               catch (e : Exception)
               {
                   call.respond(HttpStatusCode.Conflict,SimpleResponse(false,"Some Problem Occur"))
               }
           }


          delete<NoteDeleteRoute> {
                val noteId = try {
                    call.request.queryParameters["id"]!!
                }
                catch (e:Exception)
                {
                    call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"QueryParameter id is not present in database"))
                    return@delete
                }

              try {
                  val  email = call.principal<User>()!!.Email
                  db.DeleteNote(noteId,email)
                  call.respond(HttpStatusCode.OK,SimpleResponse(true,"Note Delete Successfully"))
              }
              catch (e:Exception)
              {
                  call.respond(HttpStatusCode.Conflict,SimpleResponse(false,"Some Problem is Occur"))
              }
           }

       }
}