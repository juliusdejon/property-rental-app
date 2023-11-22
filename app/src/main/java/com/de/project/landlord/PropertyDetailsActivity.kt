package com.de.project.landlord

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import com.de.project.R
import com.de.project.databinding.ActivityPropertyDetailsBinding
import com.de.project.models.Landlord
import com.de.project.models.Property
import com.de.project.properties
import com.de.project.typesList
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class PropertyDetailsActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityPropertyDetailsBinding
    var propertyId:String = ""
    var userId = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var property:Property
    lateinit var user:Landlord


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityPropertyDetailsBinding.inflate(layoutInflater)

        //needs to take a property from extras and display info appropriatly

        propertyId = intent.getStringExtra("property").toString()
        userId = intent.getStringExtra("landlord").toString()
        this.sharedPreferences= getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        for (prop in properties){
            if (prop.id == propertyId){
                property = prop
            }
        }
        for (u in landlords)
        {
            if (u.id == userId) user = u
        }

        val citiesAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, typesList
        )
        this.binding.spinnerType.adapter = citiesAdapter
        this.binding.btnSave.setOnClickListener(this)
        this.binding.btnRemove.setOnClickListener(this)

        if (propertyId != "")
        {
            this.binding.etAddress.setText(property.address)
            this.binding.etCity.setText(property.city)
            this.binding.etPostal.setText(property.postal)

            this.binding.etSpecs.setText(property.specs)
            this.binding.etDescription.setText(property.description)
            this.binding.checkAvailable.isChecked = property.available
            this.binding.spinnerType.setSelection(typesList.indexOf(property.type))
        }

        setContentView(this.binding.root)
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.btnRemove -> {
                for (property in properties)
                {
                    if (property.id == propertyId)
                    {
                        properties.remove(property)
                        user.propertiesList.remove(propertyId)
                    }
                }

                val gson = Gson()
                val propertiesAsString = gson.toJson(properties)
                this.prefEditor.putString("KEY_PROPERTIES",propertiesAsString)
                val landlordsAsString = gson.toJson(landlords)
                this.prefEditor.putString("KEY_LANDLORD",landlordsAsString)
                this.prefEditor.apply()

                val intent = Intent(this@PropertyDetailsActivity, LandlordActivity::class.java)
                intent.putExtra("landlord", userId)
                startActivity(intent)


            }

            R.id.btnSave -> {

                if (propertyId != "")
                {
                    property.type = typesList.get(this.binding.spinnerType.selectedItemPosition)
                    property.owner = property.owner
                    property.specs = this.binding.etSpecs.text.toString()
                    property.description = this.binding.etDescription.text.toString()
                    property.address = this.binding.etAddress.text.toString()
                    property.city = this.binding.etCity.text.toString()
                    property.postal =this.binding.etPostal.text.toString()
                    property.available =this.binding.checkAvailable.isChecked

                    // TODO Save current properties to Shared
                    val gson = Gson()
                    val propertiesAsString = gson.toJson(properties)
                    this.prefEditor.putString("KEY_PROPERTIES",propertiesAsString)
                    this.prefEditor.apply()

                    val intent = Intent(this@PropertyDetailsActivity, LandlordActivity::class.java)
                    intent.putExtra("landlord", userId)
                    startActivity(intent)
                }

            }
        }
    }


}