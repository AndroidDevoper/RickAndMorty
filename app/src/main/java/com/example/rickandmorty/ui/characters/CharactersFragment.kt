package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rickandmorty.data.adapter.CharacterAdapter
import com.example.rickandmorty.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

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

        binding.progressBar.visibility = View.VISIBLE

        val adapter = CharacterAdapter()
        binding.listCharacter.adapter = adapter
        viewModel.characters.observe(viewLifecycleOwner) { characters ->
            binding.progressBar.visibility = View.GONE
            if (characters != null) {
                if (characters.isEmpty()) {
                    Snackbar.make(requireView(), "Нет интернета или данные не доступны", Snackbar.LENGTH_LONG).show()
                } else {
                    adapter.submitList(characters)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}