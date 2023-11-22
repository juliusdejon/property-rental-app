package com.de.project.models
import java.util.UUID

class Property {
    val id:String = UUID.randomUUID().toString()
    var type:String
    var owner:String
    var ownerContact:String
    var specs:String
    var description:String
    var address:String
    var city:String
    var postal:String
    var available:Boolean

    constructor(type:String,owner:String,ownerContact:String,specs:String,description:String,address:String, city:String,postal:String, available:Boolean)
    {
        this.type = type
        this.owner = owner
        this.ownerContact = ownerContact
        this.specs = specs
        this.description = description
        this.address = address
        this.city = city
        this.postal = postal
        this.available = available
    }

    override fun toString(): String {
        return "Property(type='${this.type}', owner='${this.owner}', ownerContact='${this.ownerContact}',"+
                " specs='${this.specs}', description='${description}', address='${this.address}', city='${this.city}', postal='${this.postal}', available='${this.available}')"
    }
}