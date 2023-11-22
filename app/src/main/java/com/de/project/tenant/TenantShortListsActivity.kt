package com.de.project.tenant

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.de.project.PropertyAdapter
import com.de.project.R
import com.de.project.ShortListPropertyAdapter
import com.de.project.databinding.ActivityMainBinding
import com.de.project.databinding.ActivityTenantShortListsBinding
import com.de.project.models.Property
import com.de.project.models.ShortListProperty
import com.de.project.properties
import com.de.project.property.ViewPropertyActivity
import com.de.project.shortlists
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.FieldPosition

class TenantShortListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTenantShortListsBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var adapter: ShortListPropertyAdapter
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityTenantShortListsBinding.inflate(layoutInflater)
        this.sharedPreferences= getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        val tenantId = sharedPreferences.getString("KEY_TENANT_ID", "")
        val tenantShortListFromSp = sharedPreferences.getString("KEY_TENANT_SHORT_LIST_${tenantId}", "")
        if (tenantShortListFromSp!= "")
        {
            val gson = Gson()
            val typeToken = object : TypeToken<MutableList<ShortListProperty>>(){}.type
            shortlists = gson.fromJson<MutableList<ShortListProperty>>(tenantShortListFromSp,typeToken)
            val adapter: ShortListPropertyAdapter = ShortListPropertyAdapter(shortlists, {pos -> onRowClick(pos)}) { pos ->
                deleteBtnClickHandler(pos)
            }
            this.binding.rvShortListProperties.adapter = adapter
            this.binding.rvShortListProperties.layoutManager = LinearLayoutManager(this)
            binding.rvShortListProperties.addItemDecoration(
                DividerItemDecoration(
                    this,
                    LinearLayoutManager.VERTICAL
                )
            )
        }


        setContentView(this.binding.root)
    }

    private fun onRowClick(position: Int) {
        var intent = Intent(this@TenantShortListsActivity, TenantViewShortListPropertyActivity::class.java)
        intent.putExtra("EXTRA_ID", shortlists[position].id)
        startActivity(intent)
    }

    private fun deleteBtnClickHandler(position: Int) {
        val tenantId = sharedPreferences.getString("KEY_TENANT_ID", "")
        val tenantShortListFromSp = sharedPreferences.getString("KEY_TENANT_SHORT_LIST_${tenantId}", "")

        if (tenantShortListFromSp!= "")
        {
            var propertyId = shortlists[position].id
            Log.d("HEY post", "${propertyId}")

            val gson = Gson()
            val typeToken = object : TypeToken<MutableList<ShortListProperty>>(){}.type
            shortlists = gson.fromJson<MutableList<ShortListProperty>>(tenantShortListFromSp,typeToken)
            Log.d("HEY", "${shortlists}")
            var nextShortLists = mutableListOf<ShortListProperty>()
            for (short in shortlists) {
                if(short.id == propertyId) {
                } else {
                    nextShortLists.add(short)
                }
            }
            Log.d("HEY after", "${nextShortLists}")

            val nextlistAsString = gson.toJson(nextShortLists)
            Log.d("HEY z", "${nextlistAsString}")

            this.prefEditor.putString("KEY_TENANT_SHORT_LIST_${tenantId}", nextlistAsString)
            this.prefEditor.apply()
            Snackbar.make(binding.root, "${nextlistAsString}", Snackbar.LENGTH_LONG).show()
            recreate()
        }
    }

}