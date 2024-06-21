package com.example.rickandmorty.data.viewholders

import android.content.res.Resources
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.databinding.ItemCharacterBinding

class CharacterDetailHolder(private val binding: ItemCharacterBinding) :
    BaseViewHolder<CharacterVo>(binding.root) {

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
        }
    }
}