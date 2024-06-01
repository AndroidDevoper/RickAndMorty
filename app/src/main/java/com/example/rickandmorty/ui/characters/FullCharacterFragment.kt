package com.example.rickandmorty.ui.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rickandmorty.databinding.FragmentFullCharacterBinding

class FullCharacterFragment : Fragment() {
    private var _binding: FragmentFullCharacterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullCharacterBinding.inflate(inflater, container, false)


        val nameCharacter = "Подробное описание"
        binding.fullCharacter.text = nameCharacter

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}