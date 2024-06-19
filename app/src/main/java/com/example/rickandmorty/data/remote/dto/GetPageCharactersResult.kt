package com.example.rickandmorty.data.remote.dto

data class GetPageCharactersResult(
    val info: InfoDto,
    val results: List<CharacterDto>
)