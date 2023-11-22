package com.de.project.models

class Tenant(name:String,contact:String,email:String, password:String) : User(name,contact,email,password) {
    val shortList: MutableList<String> =  mutableListOf()


}