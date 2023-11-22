package com.de.project.landlord

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.de.project.R
import com.de.project.databinding.ActivityCreateLandlordBinding
import android.content.SharedPreferences
import com.de.project.models.Landlord
import com.google.gson.Gson

class CreateLandlordActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityCreateLandlordBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityCreateLandlordBinding.inflate(layoutInflater)
        this.binding.btnCreateLandlord.setOnClickListener(this)

        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()

        setContentView(this.binding.root)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btnCreateLandlord -> {
                if (this.binding.etName.text.isNullOrEmpty() ||
                    this.binding.etContact.text.isNullOrEmpty() ||
                    this.binding.etEmail.text.isNullOrEmpty() ||
                    this.binding.etPassword.text.isNullOrEmpty()
                ) {
                    if (this.binding.etName.text.isNullOrEmpty()) this.binding.etName.setError("Can't Be Empty")
                    if (this.binding.etContact.text.isNullOrEmpty()) this.binding.etContact.setError(
                        "Can't Be Empty"
                    )
                    if (this.binding.etEmail.text.isNullOrEmpty()) this.binding.etEmail.setError("Can't Be Empty")
                    if (this.binding.etPassword.text.isNullOrEmpty()) this.binding.etPassword.setError(
                        "Can't Be Empty"
                    )
                } else {
                    var alreadyExists = false
//            TODO check if email is already a LANDLORD USER
                    for (landlord in landlords) {
                        if (landlord.email == this.binding.etEmail.text.toString()) {
                            alreadyExists = true
                        }
                    }

                    if (!alreadyExists) {
                        val newLandlord = Landlord(
                            this.binding.etName.text.toString(),
                            this.binding.etContact.text.toString(),
                            this.binding.etEmail.text.toString(),
                            this.binding.etPassword.text.toString()
                        )

                        landlords.add(newLandlord)
                        val gson = Gson()
                        val landlordsAsString = gson.toJson(landlords)
                        this.prefEditor.putString("KEY_LANDLORD", landlordsAsString)
                        this.prefEditor.apply()

                        val intent =
                            Intent(this@CreateLandlordActivity, LandlordLoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        this.binding.etEmail.setError("Email Already Exists")
                    }

                }
            }
        }
    }
}