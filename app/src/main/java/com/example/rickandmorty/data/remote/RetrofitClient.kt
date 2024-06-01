package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val characterApi: RickAndMortyApi by lazy {
        retrofit.create(RickAndMortyApi::class.java)
    }
}