package com.keysersoze.brochill

import com.google.gson.annotations.SerializedName

data class UserRegistrationRequest(
    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    val email: String,

    val password: String
)


