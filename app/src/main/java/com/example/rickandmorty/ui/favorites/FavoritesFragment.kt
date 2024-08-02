package com.example.rickandmorty.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.rickandmorty.R
import com.example.rickandmorty.data.adapter.CharacterAdapter
import com.example.rickandmorty.data.remote.NetworkUtil
import com.example.rickandmorty.data.remote.NetworkUtil.showCenteredSnackbar
import com.example.rickandmorty.databinding.FragmentHomeBinding

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
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

        setupViewModel()
        setupRecyclerView()
        observeViewModel()

        if (!NetworkUtil.isInternetAvailable(requireContext())) {
            showCenteredSnackbar(view)
        }
    }

    private fun setupViewModel() {
        val factory = FavoritesViewModelFactory(requireContext().applicationContext)
        favoritesViewModel = ViewModelProvider(this, factory).get(FavoritesViewModel::class.java)
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharacterAdapter(favoritesViewModel) { characterId ->
            val bundle = Bundle().apply { putInt("characterId", characterId) }
            view?.findNavController()?.navigate(R.id.fullCharacterFragment, bundle)
        }
        binding.listCharacter.adapter = charactersAdapter
        binding.progressBar.visibility = View.GONE
    }

    private fun observeViewModel() {
        favoritesViewModel.favoriteCharacters.observe(viewLifecycleOwner) { favoriteCharacters ->
            charactersAdapter.submitList(favoriteCharacters)
        }
    }

    override fun onResume() {
        super.onResume()
        favoritesViewModel.loadFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}