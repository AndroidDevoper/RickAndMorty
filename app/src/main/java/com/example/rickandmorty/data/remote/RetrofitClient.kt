package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val characterApi = retrofit.create(RickAndMortyApi::class.java)
}