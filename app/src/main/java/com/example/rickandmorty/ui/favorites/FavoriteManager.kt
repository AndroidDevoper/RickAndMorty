package com.example.rickandmorty.ui.favorites

import android.content.Context
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.rickandmorty.R
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.vo.CharacterVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteManager(
    private val context: Context,
    private val characterRepository: CharacterRepository
) {

    suspend fun updateFavoriteButton(
        character: CharacterVo,
        favoriteButton: ImageButton,
        addToFavorites: TextView
    ) {
        val isFavorite = withContext(Dispatchers.IO) {
            characterRepository.isFavorite(character.id)
        }
        withContext(Dispatchers.Main) {
            favoriteButton.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                )
            )
            addToFavorites.text = if (isFavorite)
                context.getString(R.string.in_favorites)
            else
                context.getString(R.string.add_to_favorites)
        }
    }

    suspend fun toggleFavoriteStatus(character: CharacterVo) {
        withContext(Dispatchers.IO) {
            if (characterRepository.isFavorite(character.id)) {
                characterRepository.removeFavoriteCharacter(character.id)
            } else {
                characterRepository.addFavoriteCharacter(character)
            }
        }
    }
}