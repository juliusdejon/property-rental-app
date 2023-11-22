package com.de.project.landlord

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import com.de.project.R
import com.de.project.databinding.ActivityCreatePropertyBinding
import com.de.project.models.Landlord
import com.de.project.models.Tenant
import com.de.project.properties
import com.de.project.typesList
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class CreatePropertyActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding:ActivityCreatePropertyBinding
    private var userId = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var user:Landlord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityCreatePropertyBinding.inflate(layoutInflater)

        this.sharedPreferences= getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        userId = intent.getStringExtra("landlord").toString()
        for (landlord in landlords) {
            if (landlord.id == userId) user = landlord
        }


        val citiesAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, typesList
        )

        this.binding.createSpinner.adapter = citiesAdapter
        this.binding.btnCreate.setOnClickListener(this)

        setContentView(this.binding.root)
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btnCreate ->
            {

                if (this.binding.etAddress.text.isNullOrEmpty()) this.binding.etAddress.setError("Can't Be Empty")
                else if (this.binding.etSpecs.text.isNullOrEmpty()) this.binding.etSpecs.setError("Can't Be Empty")
                else if (this.binding.etCity.text.isNullOrEmpty()) this.binding.etCity.setError("Can't Be Empty")
                else if (this.binding.etPostal.text.isNullOrEmpty()) this.binding.etPostal.setError("Can't Be Empty")
                else
                {

                    val newProperty = com.de.project.models.Property(
                        typesList[this.binding.createSpinner.selectedItemPosition],
                        user.name, user.contact,
                        this.binding.etSpecs.text.toString(),
                        this.binding.etDescription.text.toString(),
                        this.binding.etAddress.text.toString(),
                        this.binding.etCity.text.toString(),
                        this.binding.etPostal.text.toString(),
                        this.binding.checkAvailable.isChecked
                    )

                    properties.add(newProperty)
                    user.propertiesList.add(newProperty.id)

                    val gson = Gson()
                    val propertiesAsString = gson.toJson(properties)
                    this.prefEditor.putString("KEY_PROPERTIES",propertiesAsString)
                    val landlordsAsString = gson.toJson(landlords)
                    this.prefEditor.putString("KEY_LANDLORD",landlordsAsString)
                    this.prefEditor.apply()

                    val intent = Intent(this@CreatePropertyActivity, LandlordActivity::class.java)
                    intent.putExtra("landlord", userId)
                    startActivity(intent)
                }

            }
        }
    }
}