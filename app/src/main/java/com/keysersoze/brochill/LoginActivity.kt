package com.keysersoze.brochill

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "Login Screen"

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://wern-api.brochill.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                showToast("Please enter email and password")
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val request = LoginRequest(email, password)

        val call = apiService.loginUser(request)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val token = loginResponse?.token

                    saveToken(token)

                    showToast("Login successful!")

                    val intent = Intent(this@LoginActivity, TweetActivity::class.java)
                    intent.putExtra("token", token)

                    val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    mainIntent.putExtra("token", token)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                    val taskStackBuilder = TaskStackBuilder.create(this@LoginActivity)
                    taskStackBuilder.addNextIntent(intent)

                    startActivity(taskStackBuilder.intents[0])
                    finish()
                } else {
                    val errorMessage = "Invalid email or password"
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val errorMessage = "API Call Failed: ${t.message}"
                showToast(errorMessage)
            }
        })
    }


    private fun saveToken(token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
