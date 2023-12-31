package com.de.project.models
import java.util.UUID

class Property(
    var type: String,
    var owner: String,
    var ownerContact: String,
    var specs: String,
    var description: String,
    var address: String,
    var city: String,
    var postal: String,
    var available: Boolean
) {
    val id:String = UUID.randomUUID().toString()

    override fun toString(): String {
        return "Property(id=${this.id}, type='${this.type}', owner='${this.owner}', ownerContact='${this.ownerContact}',"+
                " specs='${this.specs}', description='${description}', address='${this.address}', city='${this.city}', postal='${this.postal}', available='${this.available}')"
    }

}