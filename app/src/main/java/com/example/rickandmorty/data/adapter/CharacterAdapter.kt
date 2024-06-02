package com.example.rickandmorty.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmorty.R
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.databinding.ListItemBinding

class CharacterAdapter: ListAdapter<CharacterVo, CharacterAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)
        fun bind(character: CharacterVo) = with(binding) {
            name.text = character.name
            status.text = character.status
            species.text = character.species
            type.text = character.type
            gender.text = character.gender
            Glide.with(imageView.context)
                .load(character.image)
                .apply(RequestOptions().override(400, 400))
                .into(imageView)
        }
    }
    class Comparator : DiffUtil.ItemCallback<CharacterVo>() {
        override fun areItemsTheSame(oldItem: CharacterVo, newItem: CharacterVo): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: CharacterVo, newItem: CharacterVo): Boolean {
            return oldItem == newItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { view ->
            view.findNavController().navigate(R.id.fullCharacterFragment)
        }
    }
}