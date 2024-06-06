package com.example.rickandmorty.ui.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.NetworkUtil
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val characterRepository = CharacterRepository()
    val characters = liveData(Dispatchers.IO) {
        try {
            if (!NetworkUtil.isInternetAvailable(getApplication())) {
                throw IOException("Нет подключения к интернету")
            }
            val retrievedCharacters = characterRepository.getCharacters()
            emit(retrievedCharacters)
        } catch (e: IOException) {
            emit(emptyList())
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}