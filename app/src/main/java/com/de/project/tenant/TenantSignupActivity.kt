package com.de.project.tenant

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.de.project.R
import com.de.project.databinding.ActivityTenantSignupBinding
import com.de.project.models.Tenant
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TenantSignupActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTenantSignupBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityTenantSignupBinding.inflate(layoutInflater)

        this.binding.btnTenantSignup.setOnClickListener(this)

        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnTenantSignup ->
            {
                signup()
            }
        }
    }

    private fun signup() {
        val etName = this.binding.etTenantSignUpUsername.text.toString()
        val etContact = this.binding.etTenantSignUpContact.text.toString()
        val etEmail = this.binding.etTenantSignUpEmail.text.toString()
        val etPassword = this.binding.etTenantSignUpPassword.text.toString()

        if (
            etName.isNullOrEmpty() ||
            etContact.isNullOrEmpty() ||
            etEmail.isNullOrEmpty() ||
            etPassword.isNullOrEmpty()
        )
        {
            if (etName.isNullOrEmpty()) this.binding.etTenantSignUpUsername.setError("Name is required")
            if (etContact.isNullOrEmpty()) this.binding.etTenantSignUpContact.setError("Contact is required")
            if (etEmail.isNullOrEmpty()) this.binding.etTenantSignUpEmail.setError("Email is required")
            if (etPassword.isNullOrEmpty()) this.binding.etTenantSignUpPassword.setError("Password is required")
            Log.d("signup", "errror")
            return
        } else {
            Log.d("signup", "here")
            val tenantsListFromSP = sharedPreferences.getString("KEY_TENANTS_LIST", "")
            if (tenantsListFromSP != "") {
                val gson = Gson()
                val typeToken = object : TypeToken<MutableList<Tenant>>() {}.type
                val tenantsList = gson.fromJson<MutableList<Tenant>>(tenantsListFromSP, typeToken)

                // check if email exists in tenantsList
                var alreadyExists = false
                for (tenant in tenantsList ) {
                    if(tenant.email == etEmail) {
                        alreadyExists = true
                    }
                }
                if(alreadyExists) {
                    Log.d("signup", "exists?")
                    this.binding.etTenantSignUpEmail.setError("Email already exists")
                    return
                } else {
                    Log.d("signup", "adding")
                    val gson = Gson()
                    val typeToken = object : TypeToken<MutableList<Tenant>>() {}.type
                    val tenantsList = gson.fromJson<MutableList<Tenant>>(tenantsListFromSP, typeToken)

                    // add to list
                    var newTenant = Tenant(etName, etContact, etEmail, etPassword)
                    tenantsList.add(newTenant)
                    val listAsString = gson.toJson(tenantsList)
                    this.prefEditor.putString("KEY_TENANTS_LIST", listAsString)
                    this.prefEditor.apply()
                    Snackbar.make(binding.root, "Successfully Registered", Snackbar.LENGTH_LONG).show()
                    finish()
                }
            } else {
                var newTenant = Tenant(etName, etContact, etEmail, etPassword)

                val gson = Gson()
                val listAsString = gson.toJson(mutableListOf(newTenant))
                this.prefEditor.putString("KEY_TENANTS_LIST", listAsString)
                this.prefEditor.apply()
                Log.d("signup", "else adding")
                Snackbar.make(binding.root, "Successfully Registered", Snackbar.LENGTH_LONG).show()
                finish()
            }
        }
    }

}