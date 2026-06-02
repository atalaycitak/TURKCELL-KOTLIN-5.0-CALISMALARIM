package com.turkcell.data.repository

import com.turkcell.core.domain.AuthRepository
import com.turkcell.core.domain.AuthSession
import com.turkcell.core.domain.User
import com.turkcell.core.domain.UserRole
import com.turkcell.data.dto.CredentialsDto
import com.turkcell.data.local.TokenStore
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.util.runCatchingApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {
    override val isLoggedIn: Flow<Boolean> = tokenStore.accessToken.map { it != null }
    override val userRole: Flow<UserRole?> = tokenStore.userRole.map { UserRole.fromApi(it) }

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.login(CredentialsDto(email = email, password = password))
    }.onSuccess {
        tokenStore.save(it.accessToken, it.refreshToken, it.user.role)
    }.map { dto ->
        AuthSession(
            user = User(dto.user.id, dto.user.email, UserRole.fromApi(dto.user.role)),
            accessToken = dto.accessToken,
            refreshToken = dto.refreshToken
        )
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.register(CredentialsDto(email = email, password = password))
    }.onSuccess {
        tokenStore.save(it.accessToken, it.refreshToken, it.user.role)
    }.map { dto ->
        AuthSession(
            user = User(dto.user.id, dto.user.email, UserRole.fromApi(dto.user.role)),
            accessToken = dto.accessToken,
            refreshToken = dto.refreshToken
        )
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        tokenStore.clear()
    }
}