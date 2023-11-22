package com.de.project.property

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.de.project.R
import com.de.project.databinding.ActivityViewPropertyBinding
import com.de.project.landlord.landlords
import com.de.project.models.Property
import com.de.project.models.ShortListProperty
import com.de.project.models.Tenant
import com.de.project.properties
import com.de.project.shortlists
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewPropertyActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityViewPropertyBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    var propertyId: String? = null
    var tenantId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityViewPropertyBinding.inflate(layoutInflater)

        this.binding.btnAddToShortList.setOnClickListener(this)

        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        val propertiesFromSP = sharedPreferences.getString("KEY_PROPERTIES", "")
        val tenantIdFromSP = sharedPreferences.getString("KEY_TENANT_ID", "")

        if (propertiesFromSP != "") {
            val gson = Gson()
            val typeToken = object : TypeToken<MutableList<Property>>() {}.type
            properties = gson.fromJson<MutableList<Property>>(propertiesFromSP, typeToken)
        }
        propertyId = intent.getStringExtra("EXTRA_ID")
        tenantId = tenantIdFromSP


        //Leo
        if (propertyId != "") {
            for (i in properties) {
                if (propertyId == i.id) {

                    this.binding.propertyAddress.setText("${i.address}")
                    this.binding.propertyCity.setText("${i.city}")
                    this.binding.propertyPostal.setText("${i.postal}")
                    this.binding.propertyType.setText("Type: ${i.type}")
                    this.binding.propertySpecs.setText("${i.specs}")
                    this.binding.propertyDesc.setText("Description: ${i.description}")
                    if (i.available)
                    {
                        this.binding.propertyAvailability.setText("Available")
                        this.binding.propertyAvailability.setTextColor(Color.rgb(1,100,32))
                    }
                    else
                    {
                        this.binding.propertyAvailability.setText("Unavailable")
                        this.binding.propertyAvailability.setTextColor(Color.rgb(255,0,0))
                    }
                    this.binding.propertyContact.setText("Contact: ${i.owner} ${i.ownerContact}")
                }
            }
        } //Leo
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddToShortList ->
            {
                var property = properties.find { it.id == propertyId }
                val tenantShortListFromSp = sharedPreferences.getString("KEY_TENANT_SHORT_LIST_${tenantId}", "")
                if(tenantShortListFromSp != "") {
                    val gson = Gson()
                    val typeToken = object : TypeToken<MutableList<ShortListProperty>>() {}.type
                    val tenantPropertiesShortList =
                        gson.fromJson<MutableList<ShortListProperty>>(tenantShortListFromSp, typeToken)

                    // check if email exists in tenantsList
                    var alreadyExists = false
                    Log.d("hello", "${tenantPropertiesShortList}")
//                    Snackbar.make(binding.root, "${tenantPropertiesShortList}", Snackbar.LENGTH_LONG).show()

                    for (propz in tenantPropertiesShortList ) {
                        Log.d("propz", "${propz.id}")
                        Log.d("propertYId", "${propertyId}")
                        if(propz.id == propertyId) {
                            alreadyExists = true
                        }
                    }
                    if (alreadyExists == true) {
                        Snackbar.make(binding.root, "Already added", Snackbar.LENGTH_LONG).show()
                    } else {
                        if(property != null) {
                            val newProperty = ShortListProperty(
                                property.id,
                                property.type,
                                property.owner,
                                property.ownerContact,
                                property.specs,
                                property.description,
                                property.address,
                                property.city,
                                property.postal,
                                property.available
                            )
                            shortlists.add(newProperty)
                            val gson = Gson()
                            val propertiesAsString = gson.toJson(shortlists)
                            this.prefEditor.putString("KEY_TENANT_SHORT_LIST_${tenantId}",propertiesAsString)
                            this.prefEditor.apply()
                            Snackbar.make(binding.root, "Successfully Updated", Snackbar.LENGTH_LONG).show()
                        }
                    }
                } else {
                    if(property != null) {
                        val newProperty = ShortListProperty(
                            property.id,
                            property.type,
                            property.owner,
                            property.ownerContact,
                            property.specs,
                            property.description,
                            property.address,
                            property.city,
                            property.postal,
                            property.available
                        )
                        shortlists.add(newProperty)

                        val gson = Gson()
                        val propertiesAsString = gson.toJson(shortlists)
                        this.prefEditor.putString("KEY_TENANT_SHORT_LIST_${tenantId}",propertiesAsString)
                        this.prefEditor.apply()
                        Snackbar.make(binding.root, "Successfully Added", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
