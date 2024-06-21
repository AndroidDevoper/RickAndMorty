package com.example.rickandmorty.data.adapter

import com.example.rickandmorty.data.remote.vo.CharacterVo
import com.example.rickandmorty.data.remote.vo.LocationVo

sealed class CharacterDetailRecyclerItem {
    data class CharacterDetailInfoRecyclerItem(val character: CharacterVo) : CharacterDetailRecyclerItem()
    data class CharacterDetailLocationRecyclerItem(val location: LocationVo) : CharacterDetailRecyclerItem()
}