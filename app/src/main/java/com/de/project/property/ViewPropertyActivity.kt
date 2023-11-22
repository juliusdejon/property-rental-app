package com.de.project.property

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.de.project.R
import com.de.project.databinding.ActivityViewPropertyBinding
import com.de.project.models.Tenant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewPropertyActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityViewPropertyBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityViewPropertyBinding.inflate(layoutInflater)

        this.binding.btnAddToShortList.setOnClickListener(this)

        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddToShortList ->
            {
                val tenantShortListFromSp = sharedPreferences.getString("KEY_TENANT_SHORT_LIST", "")
                if(tenantShortListFromSp != "") {
                    val gson = Gson()
                    val typeToken = object : TypeToken<MutableList<Tenant>>() {}.type
                    val tenantShortList =
                        gson.fromJson<MutableList<Tenant>>(tenantShortListFromSp, typeToken)

                } else {
                }
            }
        }
    }
}