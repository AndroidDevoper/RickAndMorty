package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.dto.Results
import com.example.rickandmorty.data.remote.dto.Character
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickyAndMortyApi {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("id") id: Int=0,
        @Query("name") name: String="",
        @Query("status") status: String="",
        @Query("species") species: String="",
        @Query("type") type: String="",
        @Query("gender") gender: String="",
        @Query("image") image: String="",
    ): Results

    @GET("/character/{id}")
    suspend fun getCharacterId(
        @Path("id") id: Int
    ): Character
}