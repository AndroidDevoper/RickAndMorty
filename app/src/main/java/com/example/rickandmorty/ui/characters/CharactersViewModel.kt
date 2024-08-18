package com.example.rickandmorty.ui.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.adapter.CharacterAdapterItem
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.vo.CharacterVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val characterRepository: CharacterRepository = CharacterRepository(application)

    private val _characters = MutableLiveData<List<CharacterVo>>()
    private val _favoriteCharacters = MutableLiveData<List<CharacterVo>>()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _characterItems = MediatorLiveData<List<CharacterAdapterItem>>()
    val characterItems: LiveData<List<CharacterAdapterItem>> get() = _characterItems

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadCharacters()
        loadFavorites()

        _characterItems.addSource(_characters) { updateCharacterItems() }
        _characterItems.addSource(_favoriteCharacters) { updateCharacterItems() }
    }

    private fun updateCharacterItems() {
        val characters = _characters.value.orEmpty()
        val favoriteIds = _favoriteCharacters.value.orEmpty().map { it.id }

        _characterItems.value = characters.map { character ->
            CharacterAdapterItem(character, favoriteIds.contains(character.id))
        }
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