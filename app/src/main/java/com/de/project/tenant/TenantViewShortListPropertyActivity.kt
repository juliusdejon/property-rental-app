package com.de.project.tenant

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.de.project.R
import com.de.project.databinding.ActivityTenantViewShortListPropertyBinding
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

class TenantViewShortListPropertyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTenantViewShortListPropertyBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityTenantViewShortListPropertyBinding.inflate(layoutInflater)

        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        val tenantIdFromSp = sharedPreferences.getString("KEY_TENANT_ID", "")
        val tenantShortListFromSp = sharedPreferences.getString("KEY_TENANT_SHORT_LIST_${tenantIdFromSp}", "")

        if (tenantShortListFromSp != "") {
            val gson = Gson()
            val typeToken = object : TypeToken<MutableList<ShortListProperty>>() {}.type
            shortlists = gson.fromJson<MutableList<ShortListProperty>>(tenantShortListFromSp, typeToken)
            var propertyId = intent.getStringExtra("EXTRA_ID")
            Log.d("HELLO WORLD", "${propertyId}")
            Log.d("HELLO WORLD", "${shortlists}")
            if (propertyId != "") {
                for (i in shortlists) {
                    if (propertyId == i.id) {

                        this.binding.propertyAddress.setText("${i.address}")
                        this.binding.propertyType.setText("Type: ${i.type}")
                        this.binding.propertyCity.setText("${i.city}, ${i.postal}")
                        this.binding.propertySpecs.setText("Specifications: ${i.specs}")
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

                        if (i.type == "House") {
                            val imagename = "house"
                            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
                            this.binding.typeImage.setImageResource(res)
                        } else if (i.type == "Condo") {
                            val imagename = "condo"
                            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
                            this.binding.typeImage.setImageResource(res)

                        } else if (i.type == "Apartment") {
                            val imagename = "apartment"
                            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
                            this.binding.typeImage.setImageResource(res)

                        } else if (i.type == "Basement") {
                            val imagename = "basement"
                            val res = resources.getIdentifier(imagename, "drawable", this.packageName)
                            this.binding.typeImage.setImageResource(res)
                        }
                    }
                }
            } //Leo
        }

    }
}
