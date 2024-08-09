package com.example.rickandmorty.ui.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.vo.CharacterVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val characterRepository: CharacterRepository = CharacterRepository(application)
    private val _characters = MutableLiveData<List<CharacterVo>>()
    val characters: LiveData<List<CharacterVo>> get() = _characters

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = characterRepository.getCharacters(currentPage)
                if (currentPage == 1) {
                    _characters.postValue(response)
                } else {
                    val currentList = _characters.value.orEmpty()
                    _characters.postValue(currentList + response)
                }
                isLastPage = response.isEmpty()
                currentPage++
            } catch (e: IOException) {
                _error.postValue("Нет интернета или данные не доступны")
            } catch (e: Exception) {
                _error.postValue("Произошла ошибка: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadNextPage() {
        if (!isLastPage && _loading.value != true) {
            loadCharacters()
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun getCharacterById(characterId: Int): LiveData<CharacterVo?> = liveData(Dispatchers.IO) {
        try {
            val character = characterRepository.getCharacterById(characterId)
            emit(character)
        } catch (e: IOException) {
            emit(null)
            _error.postValue("Нет интернета или данные не доступны")
        } catch (e: Exception) {
            emit(null)
            _error.postValue("Произошла ошибка: ${e.message}")
        }
    }

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