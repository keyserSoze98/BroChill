package com.keysersoze.brochill

data class LoginResponse(
    val firstName: String,
    val lastName: String,
    val userId: String,
    val email: String,
    val token: String
)

