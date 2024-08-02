package com.example.rickandmorty.data.viewholders

import android.content.res.Resources
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmorty.R
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.databinding.ItemCharacterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterDetailHolder(
    private val binding: ItemCharacterBinding,
    private val repository: CharacterRepository
) : BaseViewHolder<CharacterVo>(binding.root) {

    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels

    override fun bind(item: CharacterVo) {
        with(binding) {
            Glide.with(imageView.context)
                .load(item.image)
                .apply(RequestOptions().override(screenWidth))
                .into(imageView)
            characterName.text = item.name
            characterStatus.text = item.status
            characterSpecies.text = item.species
            characterType.text = item.type
            characterGender.text = item.gender
            characterOriginName.text = item.origin.name
            characterCreated.text = item.created

            CoroutineScope(Dispatchers.Main).launch {
                updateFavoriteButton(item)
            }

            favoriteButton.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    toggleFavoriteStatus(item)
                }
            }
        }
    }

    private suspend fun updateFavoriteButton(character: CharacterVo) {
        val isFavorite = withContext(Dispatchers.IO) {
            repository.isFavorite(character.id)
        }
        binding.favoriteButton.setImageDrawable(
            ContextCompat.getDrawable(
                binding.favoriteButton.context,
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
        )
        binding.addToFavorites.text = if (isFavorite)
            binding.root.context.getString(R.string.in_favorites)
        else
            binding.root.context.getString(R.string.add_to_favorites)
    }

    private suspend fun toggleFavoriteStatus(character: CharacterVo) {
        withContext(Dispatchers.IO) {
            if (repository.isFavorite(character.id)) {
                repository.removeFavoriteCharacter(character.id)
            } else {
                repository.addFavoriteCharacter(character)
            }
        }
        updateFavoriteButton(character)
    }
}