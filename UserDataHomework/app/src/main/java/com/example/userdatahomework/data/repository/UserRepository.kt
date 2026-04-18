package com.example.userdatahomework.data.repository

import com.example.userdatahomework.data.model.User
import com.example.userdatahomework.data.remote.RetrofitInstance

class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun getUsers(): List<User> {
        return api.getUsers()
    }
}
