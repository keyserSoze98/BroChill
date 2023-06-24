package com.keysersoze.brochill

import com.google.gson.annotations.SerializedName

data class TweetResponse(
    @SerializedName("tweet")
    val tweet: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("__v")
    val version: String
)

