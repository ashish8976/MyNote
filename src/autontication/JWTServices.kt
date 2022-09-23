package com.example.autontication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.modal.User

class JWTServices
{
    private  val issuer = "noteServer"
    private val JWTSecret = "Ashish3456"
    private  val algorithm = Algorithm.HMAC512(JWTSecret)

    val varifier : JWTVerifier =
        JWT.require(algorithm)
            .withIssuer(issuer)
            .build()

    fun generateToken(user: User) : String
    {
        return  JWT.create()
            .withSubject("NoteAuthontication")
            .withIssuer(issuer)
            .withClaim("email",user.Email)
            .sign(algorithm)
    }
}