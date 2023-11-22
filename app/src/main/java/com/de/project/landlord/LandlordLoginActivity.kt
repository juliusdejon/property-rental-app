package com.de.project.landlord

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.de.project.R
import com.de.project.databinding.ActivityLandlordLoginBinding
import com.de.project.models.Landlord
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

var landlords = mutableListOf<Landlord>()

class LandlordLoginActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityLandlordLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLandlordLoginBinding.inflate(layoutInflater)

        this.binding.btnLogin.setOnClickListener(this)
        this.binding.btnNewUser.setOnClickListener(this)

        //RETRIEVE LANDLORDS FROM SHARED DATA
        this.sharedPreferences= getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        val landlordsFromSP = sharedPreferences.getString("KEY_LANDLORD","")
        if (landlordsFromSP != "")
        {
            val gson = Gson()
            val typeToken = object : TypeToken<MutableList<Landlord>>(){}.type
            landlords = gson.fromJson<MutableList<Landlord>>(landlordsFromSP,typeToken)
        }


        setContentView(this.binding.root)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnLogin ->
            {

                if (this.binding.etUsername.text.isNullOrEmpty() || this.binding.etPassword.text.isNullOrEmpty())
                {
                    if (this.binding.etUsername.text.isNullOrEmpty()) this.binding.etUsername.setError("Cannot Be Empty")
                    if (this.binding.etPassword.text.isNullOrEmpty()) this.binding.etPassword.setError("Cannot Be Empty")
                }
                else
                {
                    var found = false
                    var userId = ""
                    for(landlord in landlords)
                    {
                        if (landlord.validate(this.binding.etUsername.text.toString(),this.binding.etPassword.text.toString()))
                        {
                            found = true
                            userId = landlord.id


                            val intent = Intent(this@LandlordLoginActivity, LandlordActivity::class.java)
                            intent.putExtra("landlord", userId)
                            startActivity(intent)

                        }
                    }
                    if (!found) this.binding.btnLogin.setError("Can't Login")



                }

            }
            R.id.btnNewUser -> {
                val intent = Intent(this@LandlordLoginActivity, CreateLandlordActivity::class.java)
                startActivity(intent)
            }
        }
    }
}