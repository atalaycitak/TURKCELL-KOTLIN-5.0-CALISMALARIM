package com.example.userdatahomework.data.repository

import com.example.userdatahomework.data.model.User
import com.example.userdatahomework.data.remote.ApiService
import javax.inject.Inject

// hilt ile inject edilen repository sinifi
class UserRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getUsers(): List<User> {
        return api.getUsers()
    }
}
