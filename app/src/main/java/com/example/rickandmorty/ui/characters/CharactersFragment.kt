package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.data.adapter.CharacterAdapter
import com.example.rickandmorty.databinding.FragmentHomeBinding

class CharactersFragment : Fragment() {

    private val viewModel by viewModels<CharactersViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CharacterAdapter()
        binding.listCharacter.layoutManager = LinearLayoutManager(requireContext())
        binding.listCharacter.adapter = adapter

        viewModel.characters.observe(viewLifecycleOwner, Observer { characters ->
            adapter.submitList(characters)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}