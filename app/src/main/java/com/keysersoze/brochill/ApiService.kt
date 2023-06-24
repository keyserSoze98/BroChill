package com.keysersoze.brochill

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("welcome")
    fun getWelcomeMessage(@Header("x-api-key") apiKey: String): Call<WelcomeResponse>

    @POST("register")
    fun registerUser(
        @Body request: UserRegistrationRequest
    ): Call<UserRegistrationResponse>

    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("tweets")
    fun postTweet(
        @Header("x-api-key") token: String?,
        @Body request: TweetRequest
    ): Call<TweetResponse>

    @GET("tweets")
    fun getTweets(@Header("x-api-key") token: String?): Call<List<HomeResponse>>

}
