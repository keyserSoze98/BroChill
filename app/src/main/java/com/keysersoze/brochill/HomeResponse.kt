package com.keysersoze.brochill

data class HomeResponse(
    val tweets: List<Tweet>
)

data class Tweet(
    val tweet: String,
    val _id: String,
    val __v: Int
)


