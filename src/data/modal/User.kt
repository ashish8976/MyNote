package com.example.data.modal

import io.ktor.auth.*

data class User(

    val UserName : String,
    val Email : String,
    val Password : String

):Principal
