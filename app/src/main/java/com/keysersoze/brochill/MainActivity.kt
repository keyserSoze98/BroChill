package com.keysersoze.brochill

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var welcomeMessage: TextView
    private lateinit var startButton: Button
    private lateinit var token: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Welcome"

        welcomeMessage = findViewById(R.id.welcomeMessage)
        startButton = findViewById(R.id.startButton)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        token = retrieveToken()

        startButton.setOnClickListener {
            openUserRegistrationActivity()
        }

        fetchWelcomeMessage()
    }

    private fun fetchWelcomeMessage() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://wern-api.brochill.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getWelcomeMessage(token)

        call.enqueue(object : Callback<WelcomeResponse> {
            override fun onResponse(
                call: Call<WelcomeResponse>,
                response: Response<WelcomeResponse>
            ) {
                if (response.isSuccessful) {
                    val welcomeResponse = response.body()
                    val welcomeMessageFromApi = welcomeResponse?.message
                    println(welcomeMessage)
                    welcomeMessage.text = welcomeMessageFromApi
                } else {
                    val errorMessage = "Error: ${response.code()}"
                    println(errorMessage)
                    welcomeMessage.text = "Please register if you're a first-time user!"
                }
            }

            override fun onFailure(call: Call<WelcomeResponse>, t: Throwable) {
                val errorMessage = "API Call Failed: ${t.message}"
                showToast(errorMessage)
            }
        })
    }

    private fun openUserRegistrationActivity() {
        val intent = Intent(this, UserRegistrationActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun retrieveToken(): String {
        return sharedPreferences.getString("token", "") ?: ""
    }
}
