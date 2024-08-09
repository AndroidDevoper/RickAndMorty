package com.example.rickandmorty.ui.favorites

import androidx.lifecycle.*
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.vo.CharacterVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(private val characterRepository: CharacterRepository) : ViewModel() {

    private val _favoriteCharacters = MutableLiveData<List<CharacterVo>>()
    val favoriteCharacters: LiveData<List<CharacterVo>> get() = _favoriteCharacters

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val favorites = withContext(Dispatchers.IO) {
                characterRepository.getFavoriteCharacters()
            }
            _favoriteCharacters.postValue(favorites)
        }
    }

    fun addFavoriteCharacter(character: CharacterVo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                characterRepository.addFavoriteCharacter(character)
            }
            _favoriteCharacters.value = _favoriteCharacters.value.orEmpty() + character
        }
    }

    fun removeFavoriteCharacter(characterId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                characterRepository.removeFavoriteCharacter(characterId)
            }
            _favoriteCharacters.value = _favoriteCharacters.value?.filter { it.id != characterId }
        }
    }
}