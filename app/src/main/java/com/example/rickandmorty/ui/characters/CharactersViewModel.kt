package com.example.rickandmorty.ui.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.rickandmorty.data.remote.CharacterRepository
import kotlinx.coroutines.Dispatchers

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val characterRepository = CharacterRepository()
    val characters = liveData(Dispatchers.IO) {
        try {
            val retrievedCharacters = characterRepository.getCharacters()
            emit(retrievedCharacters)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}