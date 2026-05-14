package com.turkcell.data.di

import com.turkcell.core.domain.AuthRepository
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.repository.AuthRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "https://tickets-api.halitkalayci.com/"

val dataModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            isLenient = true
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get()
        )
    }
}