package com.example.rickandmorty.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.dto.GetPageCharactersResult
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {
    private val characterRepository = CharacterRepository()
    private val _character = MutableLiveData<List<GetPageCharactersResult>>()
    val character: LiveData<List<GetPageCharactersResult>> get() = _character

    fun loadPosts() {
        viewModelScope.launch {
            _character.value = listOf(characterRepository.getAllCharacters())
        }
    }
}