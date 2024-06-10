package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.dto.GetPageCharactersResult
import com.example.rickandmorty.data.remote.dto.LocationDto
import com.example.rickandmorty.data.remote.dto.OriginDto
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.data.remote.vo.LocationVo
import com.example.rickandmorty.data.remote.vo.OriginVo

class CharacterRepository {
    private val apiService = RetrofitClient.characterApi

    private suspend fun getAllCharacters(page: Int): GetPageCharactersResult {
        return apiService.getAllCharacters(page)
    }

    suspend fun getCharacters(currentPage: Int): List<CharacterVo> {
        val result = getAllCharacters(currentPage)
        val dtoList = result.results
        return dtoList.map { dto ->
            CharacterVo(
                id = dto.id ?: 0,
                name = dto.name ?: "",
                status = dto.status ?: "",
                species = dto.species ?: "",
                type = dto.type ?: "",
                gender = dto.gender ?: "",
                origin = mapOrigin(dto.origin),
                location = mapLocation(dto.location),
                image = dto.image ?: "",
                episode = dto.episode ?: emptyList(),
                url = dto.url ?: "",
                created = dto.created ?: ""
            )
        }
    }

    private fun mapLocation(dto: LocationDto?): LocationVo {
        return LocationVo(
            name = dto?.name ?: "",
            url = dto?.url ?: ""
        )
    }

    private fun mapOrigin(dto: OriginDto?): OriginVo {
        return OriginVo(
            name = dto?.name ?: "",
            url = dto?.url ?: ""
        )
    }

    suspend fun getCharacterById(id: Int): CharacterVo {
        val dto = apiService.getCharacterById(id)
        return dto.let {
            CharacterVo(
                id = it.id ?: 0,
                name = it.name ?: "",
                status = it.status ?: "",
                species = it.species ?: "",
                type = it.type ?: "",
                gender = it.gender ?: "",
                origin = mapOrigin(it.origin),
                location = mapLocation(it.location),
                image = it.image ?: "",
                episode = it.episode ?: emptyList(),
                url = it.url ?: "",
                created = it.created ?: ""
            )
        }
    }
}