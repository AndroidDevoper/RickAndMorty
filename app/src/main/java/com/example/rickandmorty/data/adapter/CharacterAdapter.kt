package com.example.rickandmorty.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.databinding.ListItemBinding

class CharacterAdapter(private val clickListener: (Int) -> Unit)
    : ListAdapter<CharacterVo, CharacterAdapter.CharacterViewHolder> (CharacterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CharacterViewHolder(private val binding: ListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterVo) {
            binding.name.text = character.name
            binding.status.text = character.status
            binding.species.text = character.species
            binding.type.text = character.type
            binding.gender.text = character.gender
            Glide.with(binding.imageView.context)
                .load(character.image)
                .apply(RequestOptions().override(400, 400))
                .into(binding.imageView)
            binding.root.setOnClickListener {
                clickListener(character.id)
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