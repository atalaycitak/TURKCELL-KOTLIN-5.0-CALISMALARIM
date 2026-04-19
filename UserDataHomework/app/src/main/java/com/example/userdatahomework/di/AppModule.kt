package com.example.userdatahomework.di

import com.example.userdatahomework.data.remote.ApiService
import com.example.userdatahomework.data.remote.RetrofitInstance
import com.example.userdatahomework.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // retrofit uzerinden api servisini saglar
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitInstance.api
    }

    // repository'yi api servisi ile olusturur
    @Provides
    @Singleton
    fun provideUserRepository(api: ApiService): UserRepository {
        return UserRepository(api)
    }
}
