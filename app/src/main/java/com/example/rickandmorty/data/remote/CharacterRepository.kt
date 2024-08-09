package com.example.rickandmorty.data.remote

import android.content.Context
import com.example.rickandmorty.data.remote.dto.*
import com.example.rickandmorty.data.remote.favorite.AppDatabase
import com.example.rickandmorty.data.remote.favorite.FavoriteCharacter
import com.example.rickandmorty.data.remote.vo.*

class CharacterRepository(context: Context) {

    private val apiService = RetrofitClient.characterApi
    private val favoriteDao = AppDatabase.getDatabase(context).favoriteDao()
    private fun String?.orEmpty() = this ?: ""
    private fun List<String>?.orEmptyList() = this ?: emptyList()

    private suspend fun getAllCharacters(page: Int): GetPageCharactersResult {
        return apiService.getAllCharacters(page)
    }

    suspend fun getCharacters(currentPage: Int): List<CharacterVo> {
        val result = getAllCharacters(currentPage)
        return result.results.map { it.toVo() }
    }

    private fun LocationDto?.toVo(): LocationVo {
        return LocationVo(
            name = this?.name ?: "",
            url = this?.url ?: ""
        )
    }

    private fun OriginDto?.toVo(): OriginVo {
        return OriginVo(
            name = this?.name ?: "",
            url = this?.url ?: ""
        )
    }

    private fun CharacterDto.toVo(): CharacterVo {
        return CharacterVo(
            id = id ?: 0,
            name = name ?: "",
            status = status ?: "",
            species = species ?: "",
            type = type ?: "",
            gender = gender ?: "",
            origin = origin.toVo(),
            location = location.toVo(),
            image = image ?: "",
            episode = episode ?: emptyList(),
            url = url ?: "",
            created = created ?: ""
        )
    }

    suspend fun getCharacterById(id: Int): CharacterVo {
        return apiService.getCharacterById(id).toVo()
    }

    suspend fun addFavoriteCharacter(character: CharacterVo) {
        val entity = character.toEntity()
        if (favoriteDao.getAllFavorites().none { it.id == character.id }) {
            favoriteDao.insert(entity)
        }
    }

    suspend fun removeFavoriteCharacter(characterId: Int) {
        favoriteDao.getAllFavorites().find { it.id == characterId }?.let {
            favoriteDao.delete(it)
        }
    }

    fun getFavoriteCharacters(): List<CharacterVo> {
        return favoriteDao.getAllFavorites().map { it.toVo() }
    }

    suspend fun isFavorite(characterId: Int): Boolean {
        return favoriteDao.isFavorite(characterId)
    }

    private fun FavoriteCharacter.toVo(): CharacterVo {
        return CharacterVo(
            id = id,
            name = name.orEmpty(),
            status = status.orEmpty(),
            species = species.orEmpty(),
            type = type.orEmpty(),
            gender = gender.orEmpty(),
            origin = OriginVo(originName.orEmpty(), originUrl.orEmpty()),
            location = LocationVo(locationName.orEmpty(), locationUrl.orEmpty()),
            image = image.orEmpty(),
            episode = episode.orEmptyList(),
            url = url.orEmpty(),
            created = created.orEmpty()
        )
    }

    private fun CharacterVo.toEntity(): FavoriteCharacter {
        return FavoriteCharacter(
            id = id,
            name = name,
            status = status,
            species = species,
            type = type,
            gender = gender,
            originName = origin.name,
            originUrl = origin.url,
            locationName = location.name,
            locationUrl = location.url,
            image = image,
            episode = episode,
            url = url,
            created = created
        )
    }
}