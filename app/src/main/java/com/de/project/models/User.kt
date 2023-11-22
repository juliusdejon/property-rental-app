package com.de.project.models
import java.util.UUID

open class User (var name:String,var contact:String,var email:String,var password:String){
    val id:String = UUID.randomUUID().toString()
    fun validate (useremail:String, userpassword:String) : Boolean
    {
        return useremail == this.email && this.password == userpassword
    }
}

