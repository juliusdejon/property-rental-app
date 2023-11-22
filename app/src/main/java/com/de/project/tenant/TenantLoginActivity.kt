package com.de.project.tenant

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.de.project.R
import com.de.project.databinding.ActivityTenantLoginBinding
import com.de.project.models.Tenant
import com.de.project.property.ViewPropertyActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay

class TenantLoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTenantLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityTenantLoginBinding.inflate(layoutInflater)

        this.binding.btnTenantLogin.setOnClickListener(this)
        this.binding.btnTenantNewUser.setOnClickListener(this)

        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        id = intent.getStringExtra("EXTRA_ID")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnTenantLogin ->
            {
                login()
            }
            R.id.btnTenantNewUser -> {
                val intent = Intent(this@TenantLoginActivity, TenantSignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun login() {
        val etName = this.binding.etTenantUsername.text.toString()
        val etPassword = this.binding.etTenantPassword.text.toString()

        if(etName.isNullOrEmpty() || etPassword.isNullOrEmpty()) {
            if (etName.isNullOrEmpty()) this.binding.etTenantUsername.setError("Username is required")
            if (etPassword.isNullOrEmpty()) this.binding.etTenantPassword.setError("Password is required")
            return;
        } else {
            val tenantsListFromSP = sharedPreferences.getString("KEY_TENANTS_LIST", "")
            if(tenantsListFromSP != "") {
                val gson = Gson()
                val typeToken = object : TypeToken<MutableList<Tenant>>() {}.type
                val tenantsList = gson.fromJson<MutableList<Tenant>>(tenantsListFromSP, typeToken)

                var alreadyExists = false
                var tenantId: String? = null
                for (tenant in tenantsList ) {
                    if(tenant.email == etName && tenant.password == etPassword) {
                        alreadyExists = true
                        tenantId = tenant.id
                    }
                }
                if (alreadyExists) {
                    this.prefEditor.putString("KEY_IS_LOGGED_IN", "true")
                    this.prefEditor.putString("KEY_TENANT_ID", tenantId)
                    this.prefEditor.apply()
                    Snackbar.make(binding.root, "Successfully login. You can now add to short list", Snackbar.LENGTH_INDEFINITE)
                        .show()
                    finishAfterTransition()
                } else {
                    Snackbar.make(binding.root, "Invalid username or password", Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(binding.root, "Invalid username or password", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}