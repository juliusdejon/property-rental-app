package com.de.project.landlord

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.de.project.MainActivity
import com.de.project.PropertyAdapter
import com.de.project.R
import com.de.project.databinding.ActivityLandlordBinding
import com.de.project.models.Landlord
import com.de.project.models.Property
import com.de.project.properties
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LandlordActivity : AppCompatActivity(),OnClickListener {
    private lateinit var binding: ActivityLandlordBinding
    private var userId = ""
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var user:Landlord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLandlordBinding.inflate(layoutInflater)

        //get data
        this.sharedPreferences= getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()
        val propertiesFromSP = sharedPreferences.getString("KEY_PROPERTIES","")
        if (propertiesFromSP!= "")
        {
            val gson = Gson()
            val typeToken = object : TypeToken<MutableList<Property>>(){}.type
            properties = gson.fromJson<MutableList<Property>>(propertiesFromSP,typeToken)
        }
        val landlordsProperties = mutableListOf<Property>()
        userId = intent.getStringExtra("landlord").toString()

        for (landlord in landlords){
            if (landlord.id == userId)
            {
                user = landlord
                this.binding.tvLandlordTitle.setText("${user.name}'s Properties")

                //
                for (id in user.propertiesList)
                {
                    for (property in properties)
                    {
                        if (id == property.id) landlordsProperties.add(property)
                    }
                }

            }
        }

        this.binding.btnCreateProperty.setOnClickListener(this)
        this.binding.btnLandlordLogout.setOnClickListener(this)

        // properties to display in RV
        val adapter: PropertyAdapter = PropertyAdapter(landlordsProperties) { pos -> rowClicked(pos) }
        this.binding.rvProperties.adapter=adapter
        this.binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        setContentView(this.binding.root)
    }

    private fun rowClicked(pos: Int) {
        val intent = Intent(this@LandlordActivity, PropertyDetailsActivity::class.java)
        intent.putExtra("landlord",userId)
        intent.putExtra("property",user.propertiesList[pos])
        startActivity(intent)

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btnCreateProperty ->{
                val intent = Intent(this@LandlordActivity, CreatePropertyActivity::class.java)
                intent.putExtra("landlord",userId)
                startActivity(intent)
            }
            R.id.btnLandlordLogout -> {
                val intent = Intent(this@LandlordActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}