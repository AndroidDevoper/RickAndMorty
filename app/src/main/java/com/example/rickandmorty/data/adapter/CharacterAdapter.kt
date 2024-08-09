package com.example.rickandmorty.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.ListItemBinding

class CharacterAdapter(
    private val clickListener: (Int) -> Unit,
    private val favoriteClickListener: (CharacterAdapterItem) -> Unit
) : ListAdapter<CharacterAdapterItem, CharacterAdapter.CharacterViewHolder>(CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CharacterViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterAdapterItem) {
            with(binding) {
                val character = item.character

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

                favoriteButton.setImageResource(
                    if (item.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                )
                addToFavorites.text = if (item.isFavorite)
                    root.context.getString(R.string.in_favorites)
                else
                    root.context.getString(R.string.add_to_favorites)

                favoriteButton.setOnClickListener {
                    favoriteClickListener(item)
                }

                addToFavorites.setOnClickListener {
                    favoriteClickListener(item)
                }
            }
        }
    }

    class CharacterDiffCallback : DiffUtil.ItemCallback<CharacterAdapterItem>() {
        override fun areItemsTheSame(oldItem: CharacterAdapterItem, newItem: CharacterAdapterItem): Boolean {
            return oldItem.character.id == newItem.character.id
        }

        override fun areContentsTheSame(oldItem: CharacterAdapterItem, newItem: CharacterAdapterItem): Boolean {
            return oldItem == newItem
        }
    }
}