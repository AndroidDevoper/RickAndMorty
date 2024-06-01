package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.dto.GetPageCharactersResult

class CharacterRepository {
    private val apiService = RetrofitClient.characterApi

    suspend fun getAllCharacters(): GetPageCharactersResult {
        return apiService.getAllCharacters()
    }
}