package com.example.rickandmorty.data.remote

import com.example.rickandmorty.data.remote.dto.GetPageCharactersResult
import com.example.rickandmorty.data.remote.dto.LocationDto
import com.example.rickandmorty.data.remote.dto.OriginDto
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.data.remote.vo.LocationVo
import com.example.rickandmorty.data.remote.vo.OriginVo

class CharacterRepository {
    private val apiService = RetrofitClient.characterApi

    suspend fun getAllCharacters(): GetPageCharactersResult {
        return apiService.getAllCharacters()
    }

    suspend fun getCharacters(): List<CharacterVo>? {
        val result = getAllCharacters() // Получаем результат из API
        val dtoList = result.results  // Извлекаем список CharacterDto
        return dtoList.map { dto ->  // Преобразуем список CharacterDto в список CharacterVo
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
}