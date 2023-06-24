package com.keysersoze.brochill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UserRegistrationActivity : AppCompatActivity() {

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var goToLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)
        supportActionBar?.title = "Signup"

        firstNameEditText = findViewById(R.id.firstNameEditText)
        lastNameEditText = findViewById(R.id.lastNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.nextButton)
        goToLoginButton = findViewById(R.id.goToLoginButton)

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(firstName, lastName, email, password)
            } else {
                showToast("Please fill in all the fields")
            }
        }

        goToLoginButton.setOnClickListener {
            navigateToLoginActivity()
        }
    }

    private fun registerUser(firstName: String, lastName: String, email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(" https://wern-api.brochill.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val request = UserRegistrationRequest(firstName, lastName, email, password)

        val call = apiService.registerUser(request)
        call.enqueue(object : Callback<UserRegistrationResponse> {
            override fun onResponse(
                call: Call<UserRegistrationResponse>,
                response: Response<UserRegistrationResponse>
            ) {
                if (response.isSuccessful) {
                    val userRegistrationResponse = response.body()
                    val firstName = userRegistrationResponse?.firstName
                    val lastName = userRegistrationResponse?.lastName
                    val userId = userRegistrationResponse?.userId
                    val email = userRegistrationResponse?.email
                    val token = userRegistrationResponse?.token

                    showToast("User registered successfully!\n" +
                            "First Name: $firstName\n" +
                            "Last Name: $lastName\n" +
                            "User ID: $userId\n" +
                            "Email: $email\n" +
                            "Token: $token")

                    val intent = Intent(this@UserRegistrationActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = "Error: ${response.code()}"
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<UserRegistrationResponse>, t: Throwable) {
                val errorMessage = "API Call Failed: ${t.message}"
                showToast(errorMessage)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

