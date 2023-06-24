package com.keysersoze.brochill

import com.google.gson.annotations.SerializedName

data class WelcomeResponse(
    @SerializedName("message")
    val message: String
)

