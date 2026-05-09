package com.turkcell.authrepowithkoin

class AuthRepository(private val authService: AuthService) {
    suspend fun login(email: String, password: String): LoginResponse {
        return authService.login(LoginRequest(email, password))
    }
}
