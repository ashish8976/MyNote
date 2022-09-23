package com.example.route

import com.example.autontication.JWTServices
import com.example.data.modal.LoginRequest
import com.example.data.modal.RegisterRequest
import com.example.data.modal.SimpleResponse
import com.example.data.modal.User
import com.example.repository.UserRepo
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*



const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"


@Location(REGISTER_REQUEST)
class UserRegisterRoute

@Location(LOGIN_REQUEST)
class UserLoginRoute



fun Route.UserRoute(
    db : UserRepo,
    jwtServices: JWTServices,
    hashFunction: (String) -> String)
{
    // Register Route
    post<UserRegisterRoute> {
          val registerRequest =
              try {
                  call.receive<RegisterRequest>()
              }
              catch (e :Exception)
              {
                  call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Some Fields"))
                  return@post
              }

         try {
           val user = User(registerRequest.Username,registerRequest.Email,hashFunction(registerRequest.Password))
             db.addUser(user)
             // this code is provide unique token for all users in database
             call.respond(HttpStatusCode.OK,SimpleResponse(true,jwtServices.generateToken(user)))
         }

         catch (e : Exception)
         {
            call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?:"Some Problem Occured"))
         }

    }


    // Login Route
    post<UserLoginRoute> {
           val loginRequest = try {
               call.receive<LoginRequest>()
           }
           catch (e : Exception)
           {
               call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Some Fields"))
               return@post
           }

        try {
            val user = db.findUserByEmail(loginRequest.Email)
            if (user == null)
            {
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Wrong Email id"))
            }
            else
            {
                if (user.Password == hashFunction(loginRequest.Password))
                {
                    call.respond(HttpStatusCode.OK,SimpleResponse(true,jwtServices.generateToken(user)))
                }
                else
                {
                    call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Wrong Password"))
                }
            }
        }
        catch (e : Exception)
        {
            call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?:"Some Problem Occured"))
        }
    }
}
