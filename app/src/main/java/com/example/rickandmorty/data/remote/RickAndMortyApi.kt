package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.dto.CharacterDto
import com.example.rickandmorty.data.remote.dto.GetPageCharactersResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getAllCharacters(
        @Query("pages") page: Int? = null,
        @Query("id") id: Int? = null,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null,
        @Query("image") image: String? = null,
    ): GetPageCharactersResult

    @GET("/character/{id}")
    fun getCharacterId(
        @Path("id") id: Int
    ): CharacterDto
}
