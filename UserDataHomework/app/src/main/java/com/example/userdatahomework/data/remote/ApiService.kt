package com.example.userdatahomework.data.remote

import com.example.userdatahomework.data.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
