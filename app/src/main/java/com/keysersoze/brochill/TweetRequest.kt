package com.keysersoze.brochill

import com.google.gson.annotations.SerializedName

data class TweetRequest(
    @SerializedName("tweet")
    val tweet: String
)

