package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.data.remote.dto.GetPageCharactersResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    fun getPage(
        @Query("pages") page: Int = 0
    ): Call<GetPageCharactersResult>

    @GET("character")
    suspend fun getAllCharacters(
        @Query("id") id: Int = 0,
        @Query("name") name: String = "",
        @Query("status") status: String = "",
        @Query("species") species: String = "",
        @Query("type") type: String = "",
        @Query("gender") gender: String = "",
        @Query("image") image: String = "",
    ): GetPageCharactersResult

    @GET("/character/{id}")
    fun getCharacterId(
        @Path("id") id: Int
    ): CharacterDto
}
