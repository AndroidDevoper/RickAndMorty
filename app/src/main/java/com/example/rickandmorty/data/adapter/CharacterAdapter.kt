package com.example.rickandmorty.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmorty.R
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.databinding.ListItemBinding
import com.example.rickandmorty.ui.favorites.FavoritesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterAdapter(
    private val viewModel: FavoritesViewModel,
    private val clickListener: (Int) -> Unit
) : ListAdapter<CharacterVo, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CharacterViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: CharacterVo) {
            with(binding) {
                name.text = character.name
                status.text = character.status
                species.text = character.species
                type.text = character.type
                gender.text = character.gender

                Glide.with(imageView.context)
                    .load(character.image)
                    .apply(RequestOptions().override(400, 400))
                    .into(imageView)

                root.setOnClickListener {
                    clickListener(character.id)
                }

                updateFavoriteStatus(character)

                favoriteButton.setOnClickListener {
                    toggleFavorite(character)
                }

                addToFavorites.setOnClickListener {
                    toggleFavorite(character)
                }
            }
        }

        private fun updateFavoriteStatus(character: CharacterVo) {
            CoroutineScope(Dispatchers.Main).launch {
                val isFavorite = withContext(Dispatchers.IO) {
                    viewModel.isFavorite(character.id)
                }
                withContext(Dispatchers.Main) {
                    binding.favoriteButton.setImageResource(
                        if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    )
                    binding.addToFavorites.text = if (isFavorite)
                        binding.root.context.getString(R.string.in_favorites)
                    else
                        binding.root.context.getString(R.string.add_to_favorites)
                }
            }
        }

        private fun toggleFavorite(character: CharacterVo) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    if (viewModel.isFavorite(character.id)) {
                        viewModel.removeFavoriteCharacter(character.id)
                    } else {
                        viewModel.addFavoriteCharacter(character)
                    }
                }
                updateFavoriteStatus(character)
            }
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<CharacterVo>() {
        override fun areItemsTheSame(oldItem: CharacterVo, newItem: CharacterVo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharacterVo, newItem: CharacterVo): Boolean {
            return oldItem == newItem
        }
    }
}