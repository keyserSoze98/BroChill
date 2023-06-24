package com.keysersoze.brochill

import com.google.gson.annotations.SerializedName

data class UserRegistrationResponse(
    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("_id")
    val userId: String,

    val email: String,

    val token: String
)



