package com.example

import com.example.autontication.JWTServices
import com.example.autontication.hash
import com.example.repository.Database_Factory
import com.example.repository.NoteRepo
import com.example.repository.UserRepo
import com.example.route.NoteRoute
import com.example.route.UserRoute
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads

fun Application.module(testing: Boolean = false) {

    Database_Factory.init()

    val db = UserRepo()
    val db2 = NoteRepo()
    val JWTService = JWTServices()
    val hashFunction = {s : String -> hash(s) }


    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

  install(Locations)

    install(Authentication) {
        jwt("jwt") {
            verifier(JWTService.varifier)
            realm = "NoteServer"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                    user
            }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        UserRoute(db,JWTService,hashFunction)
        NoteRoute(db2,hashFunction)


// Path Parameter
        // localhost:/notes
        route("/notes")
        {

            // localhost:8093/notes/create
            route("/create")
            {
                post {
                    val body = call.receive<String>()
                    call.respond(body)
                }
            }

            // localhost:8093/notes
            delete {
                 val body = call.receive<String>()
                call.respond(body)
            }
        }


    }
}

data class MySession(val count: Int = 0)


//get("/note/{id}")
//{
//    val id = call.parameters["id"]
//    call.respond("${id}")
//}
//
//
//get("/token")
//{
//    val email = call.request.queryParameters["email"]!!
//    val password = call.request.queryParameters["password"]!!
//    val username = call.request.queryParameters["username"]!!
//
//    val user =User(email,hashFunction(password),username)
//
//    call.respond(JWTService.generateToken(user))
//}
//
//
//get("/note") {
//    val id =  call.request.queryParameters["id"]
//    call.respond("${id}")
//}

