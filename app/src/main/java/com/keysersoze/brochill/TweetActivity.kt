package com.keysersoze.brochill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TweetActivity : AppCompatActivity() {
    private lateinit var tweetEditText: EditText
    private lateinit var postButton: Button
    private lateinit var homeButton: Button

    private val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://wern-api.brochill.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)
        supportActionBar?.title = "New Tweet"

        tweetEditText = findViewById(R.id.tweetEditText)
        postButton = findViewById(R.id.postButton)
        homeButton = findViewById(R.id.homeButton)

        homeButton.setOnClickListener {
            navigateToHomeActivity()
        }

        postButton.setOnClickListener {
            val tweet = tweetEditText.text.toString().trim()
            if (tweet.isNotEmpty()) {
                val token = intent.getStringExtra("token")
                println(token)
                val request = TweetRequest(tweet)

                apiService.postTweet(token, request).enqueue(object : Callback<TweetResponse> {
                    override fun onResponse(call: Call<TweetResponse>, response: Response<TweetResponse>) {
                        println(request)
                        println(response)
                        if (response.isSuccessful) {
                            // Tweet posted successfully
                            Toast.makeText(this@TweetActivity, "Tweet posted successfully", Toast.LENGTH_SHORT).show()
                            navigateToHomeActivity()
                        } else {
                            // Failed to post tweet
                            Toast.makeText(this@TweetActivity, "Failed to post tweet", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<TweetResponse>, t: Throwable) {
                        // Handle API call failure
                        Toast.makeText(this@TweetActivity, "API call failed", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter a tweet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHomeActivity() {
        val intent1 = Intent(this, HomeActivity::class.java)
        val token = intent.getStringExtra("token")
        intent1.putExtra("token", token)
        startActivity(intent1)
    }
}

