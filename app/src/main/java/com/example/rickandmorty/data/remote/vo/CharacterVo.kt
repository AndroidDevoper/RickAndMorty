package com.example.rickandmorty.data.remote.vo

import com.example.rickandmorty.data.remote.dto.LocationDto
import com.example.rickandmorty.data.remote.dto.OriginDto

data class CharacterVo(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender:String,
    val origin: OriginVo,
    val location: LocationVo,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)
