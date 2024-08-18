package com.example.rickandmorty.data.adapter

import com.example.rickandmorty.data.remote.vo.CharacterVo

data class CharacterAdapterItem(
    val character: CharacterVo,
    val isFavorite: Boolean
)