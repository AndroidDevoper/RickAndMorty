package com.example.rickandmorty.data.remote.dto

data class GetPageCharactersResult(
    val info: Info,
    val results: List<CharacterDto>
)