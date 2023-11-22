package com.de.project

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.de.project.landlord.LandlordLoginActivity
import com.de.project.databinding.ActivityMainBinding
import com.de.project.models.Property
import com.de.project.tenant.TenantLoginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

var properties = mutableListOf<Property>()


val typesList:List<String> = listOf("Condo","House","Apartment","Basement")

class MainActivity : AppCompatActivity(),OnClickListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        this.sharedPreferences= getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()



        val propertiesFromSP = sharedPreferences.getString("KEY_PROPERTIES","")
        if (propertiesFromSP!= "")
        {
            val gson = Gson()
            val typeToken = object : TypeToken<MutableList<Property>>(){}.type
            properties = gson.fromJson<MutableList<Property>>(propertiesFromSP,typeToken)
        }





        val adapter:PropertyAdapter = PropertyAdapter(properties) { pos -> rowClicked(pos) }
        this.binding.rvProperties.adapter=adapter
        this.binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        this.binding.btnSearch.setOnClickListener(this)
        this.binding.btnLandlord.setOnClickListener(this)



        setContentView(this.binding.root)
    }

    fun rowClicked(position: Int)
    {
        val intent = Intent(this@MainActivity, TenantLoginActivity::class.java)
        intent.putExtra("EXTRA_POSITION", position)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.btnSearch ->
            {
                if (this.binding.etSearch.text.isNullOrEmpty())
                {
                    this.binding.etSearch.setError("Cannot Be Empty")
                    val adapter:PropertyAdapter = PropertyAdapter(properties) { pos -> rowClicked(pos) }
                    this.binding.rvProperties.adapter=adapter
                }
                else
                {
                    //check name, city, postal code
                    val search = binding.etSearch.text.toString()
                    val searchedProperties = mutableListOf<Property>()

                    for (property in properties)
                    {
                        if (property.city == search || property.postal == search || property.address == search)
                        {
                            searchedProperties.add(property)
                        }
                    }

                    val adapter:PropertyAdapter = PropertyAdapter(searchedProperties) { pos -> rowClicked(pos) }
                    this.binding.rvProperties.adapter=adapter
                }
            }
            R.id.btnLandlord -> {
                val intent = Intent(this@MainActivity, LandlordLoginActivity::class.java)
                startActivity(intent)
            }
        }
    }


}