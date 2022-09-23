package com.example.autontication

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private  val hashkey = System.getenv("HASE_SECRET_KEY").toByteArray()
private  val hmacKey = SecretKeySpec(hashkey,"HmacSha1")


fun hash(password : String):String{
    val hmac = Mac.getInstance("HmacSha1")
    hmac.init(hmacKey)
    return  hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
