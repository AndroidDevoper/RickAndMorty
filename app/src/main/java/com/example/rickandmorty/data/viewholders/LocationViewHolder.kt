package com.example.rickandmorty.data.viewholders

import com.example.rickandmorty.data.remote.vo.LocationVo
import com.example.rickandmorty.databinding.ItemLocationBinding

class LocationViewHolder(private val binding: ItemLocationBinding):
    BaseViewHolder<LocationVo>(binding.root) {
    override fun bind(item: LocationVo) {
        binding.locationName.text = item.name
    }
}