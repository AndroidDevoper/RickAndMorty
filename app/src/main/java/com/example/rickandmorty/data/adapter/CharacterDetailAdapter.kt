package com.example.rickandmorty.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.data.adapter.CharacterDetailRecyclerItem.*
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.data.viewholders.BaseViewHolder
import com.example.rickandmorty.data.viewholders.CharacterDetailHolder
import com.example.rickandmorty.data.viewholders.LocationViewHolder
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.example.rickandmorty.databinding.ItemLocationBinding

class CharacterDetailAdapter : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private val items = mutableListOf<CharacterDetailRecyclerItem>()

    companion object {
        private const val VIEW_TYPE_CHARACTER = 0
        private const val VIEW_TYPE_LOCATION = 1
    }

    fun setData(character: CharacterVo) {
        items.clear()
        items.add(CharacterDetailInfoRecyclerItem(character))
        items.add(CharacterDetailLocationRecyclerItem(character.location))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_CHARACTER -> {
                val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CharacterDetailHolder(binding)
            }
            VIEW_TYPE_LOCATION -> {
                val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LocationViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CharacterDetailInfoRecyclerItem -> VIEW_TYPE_CHARACTER
            is CharacterDetailLocationRecyclerItem -> VIEW_TYPE_LOCATION
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (val item = items[position]) {
            is CharacterDetailInfoRecyclerItem -> (holder as CharacterDetailHolder).bind(item.character)
            is CharacterDetailLocationRecyclerItem -> (holder as LocationViewHolder).bind(item.location)
        }
    }
}