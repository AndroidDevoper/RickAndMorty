package com.example.rickandmorty.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.data.adapter.CharacterAdapter
import com.example.rickandmorty.data.adapter.CharacterAdapterItem
import com.example.rickandmorty.data.remote.NetworkUtil
import com.example.rickandmorty.data.remote.NetworkUtil.showCenteredSnackbar
import com.example.rickandmorty.databinding.FragmentHomeBinding

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(requireContext().applicationContext)
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var charactersAdapter: CharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

    }

    private fun setupRecyclerView() {
        charactersAdapter = CharacterAdapter(
            clickListener = { characterId ->
                val bundle = Bundle().apply { putInt("characterId", characterId) }
                view?.findNavController()?.navigate(R.id.fullCharacterFragment, bundle)
            },
            favoriteClickListener = { item ->
                if (item.isFavorite) {
                    favoritesViewModel.removeFavoriteCharacter(item.character.id)
                } else {
                    favoritesViewModel.addFavoriteCharacter(item.character)
                }
                val updatedList = charactersAdapter.currentList.map {
                    if (it == item) it.copy(isFavorite = !it.isFavorite) else it
                }
                charactersAdapter.submitList(updatedList)
            }
        )
        binding.listCharacter.adapter = charactersAdapter
        binding.progressBar.visibility = View.GONE
    }

    private fun observeViewModel() {
        favoritesViewModel.favoriteCharacters.observe(viewLifecycleOwner) { favoriteCharacters ->
            val characterItems = favoriteCharacters.map { character ->
                CharacterAdapterItem(character, true)
            }
            charactersAdapter.submitList(characterItems)
        }
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.loadFavorites()

        if (!NetworkUtil.isInternetAvailable(requireContext())) {
            view?.let { showCenteredSnackbar(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}