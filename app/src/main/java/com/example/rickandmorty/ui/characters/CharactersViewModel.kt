package com.example.rickandmorty.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.rickandmorty.data.remote.CharacterRepository
import kotlinx.coroutines.Dispatchers

class CharactersViewModel : ViewModel() {
    private val characterRepository = CharacterRepository()
    val characters = liveData(Dispatchers.IO) {
        val retrievedCharacters = characterRepository.getCharacters()
        emit(retrievedCharacters)
    }
}