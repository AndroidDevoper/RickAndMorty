package com.example.rickandmorty.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rickandmorty.data.adapter.CharacterDetailRecyclerItem.*
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.data.viewholders.BaseViewHolder
import com.example.rickandmorty.data.viewholders.CharacterDetailHolder
import com.example.rickandmorty.data.viewholders.LocationViewHolder
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.example.rickandmorty.databinding.ItemLocationBinding

class CharacterDetailAdapter (private val repository: CharacterRepository): ListAdapter<CharacterDetailRecyclerItem,
        BaseViewHolder<*>>(CharacterDetailDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_CHARACTER = 0
        private const val VIEW_TYPE_LOCATION = 1
    }

    fun setData(character: CharacterVo) {
        val items = listOf(
            CharacterDetailInfoRecyclerItem(character),
            CharacterDetailLocationRecyclerItem(character.location)
        )
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_CHARACTER -> {
                val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CharacterDetailHolder(binding, repository)
            }
            VIEW_TYPE_LOCATION -> {
                val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LocationViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CharacterDetailInfoRecyclerItem -> VIEW_TYPE_CHARACTER
            is CharacterDetailLocationRecyclerItem -> VIEW_TYPE_LOCATION
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (val item = getItem(position)) {
            is CharacterDetailInfoRecyclerItem -> (holder as CharacterDetailHolder).bind(item.character)
            is CharacterDetailLocationRecyclerItem -> (holder as LocationViewHolder).bind(item.location)
        }
    }

    class CharacterDetailDiffCallback : DiffUtil.ItemCallback<CharacterDetailRecyclerItem>() {
        override fun areItemsTheSame(
            oldItem: CharacterDetailRecyclerItem,
            newItem: CharacterDetailRecyclerItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CharacterDetailRecyclerItem,
            newItem: CharacterDetailRecyclerItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}