package com.de.project.models

class Landlord(name:String,contact:String,email:String, password:String) : User(name,contact,email,password) {
    val propertiesList: MutableList<String> =  mutableListOf()

    override fun toString(): String {
        var output = "Landlord(name='${this.name}', contact='${this.contact}', email='${this.email}', password='${this.password}', "+
                "propertiesList=["
        for (pos in propertiesList)
        {
            output += "${pos}, "
        }
        output = output.dropLast(2)
        output +="])"
        return output
    }

}