package com.turkcell.authrepowithkoin

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(AuthService::class.java)
    }

    single {
        AuthRepository(get())
    }

    viewModel {
        LoginViewModel(get())
    }
}
