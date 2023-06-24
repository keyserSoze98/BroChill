package com.keysersoze.brochill

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import okhttp3.*

class HomeActivity : AppCompatActivity() {

    private val TAG: String = HomeActivity::class.java.simpleName
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private lateinit var apiKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = "Home Screen"

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HomeAdapter()
        recyclerView.adapter = adapter

        apiKey = intent.getStringExtra("token") ?: ""
        println(apiKey)

        fetchTweets()

        val addButton: FloatingActionButton = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(this, TweetActivity::class.java)
            intent.putExtra("token", apiKey)
            startActivity(intent)
        }
    }

    private fun fetchTweets() {
        FetchTweetsTask().execute()
    }
    private inner class FetchTweetsTask : AsyncTask<Void, Void, List<Tweet>>() {

        override fun doInBackground(vararg params: Void): List<Tweet>? {
            val url = "https://wern-api.brochill.app/tweets"
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .addHeader("x-api-key", apiKey)
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val tweetList = gson.fromJson(responseData, Array<Tweet>::class.java).toList()
                    val reversedList = tweetList.reversed()
                    return reversedList
                } else {
                    Log.e(TAG, "Failed to fetch tweets: ${response.code}")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error fetching tweets: ${e.message}")
            }

            return null
        }

        override fun onPostExecute(tweetList: List<Tweet>?) {
            super.onPostExecute(tweetList)

            if (tweetList != null) {
                adapter.setTweets(tweetList)
            } else {
                Log.e(TAG, "Failed to fetch tweets")
            }
        }
    }

}
