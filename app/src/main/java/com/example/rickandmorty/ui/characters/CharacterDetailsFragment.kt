package com.example.rickandmorty.ui.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.rickandmorty.data.adapter.CharacterDetailAdapter
import com.example.rickandmorty.data.adapter.CharacterDetailRecyclerItem
import com.example.rickandmorty.data.remote.CharacterRepository
import com.example.rickandmorty.data.remote.NetworkUtil
import com.example.rickandmorty.data.remote.NetworkUtil.showCenteredSnackbar
import com.example.rickandmorty.databinding.FragmentHomeBinding
import com.example.rickandmorty.ui.favorites.FavoriteManager

class CharacterDetailsFragment : Fragment() {

    private val viewModel by viewModels<CharactersViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CharacterDetailAdapter
    private lateinit var favoriteManager: FavoriteManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterRepository = CharacterRepository(requireContext())
        favoriteManager = FavoriteManager(requireContext(), characterRepository)
        adapter = CharacterDetailAdapter(favoriteManager)
        binding.listCharacter.adapter = adapter
        binding.progressBar.visibility = View.VISIBLE

        val characterId = arguments?.getInt("characterId") ?: return
        viewModel.getCharacterById(characterId).observe(viewLifecycleOwner) { character ->
            binding.progressBar.visibility = View.GONE
            if (!NetworkUtil.isInternetAvailable(requireContext())) {
                showCenteredSnackbar(view)
            } else {
                character?.let {
                    val items = listOf(
                        CharacterDetailRecyclerItem.CharacterDetailInfoRecyclerItem(it),
                        CharacterDetailRecyclerItem.CharacterDetailLocationRecyclerItem(it.location)
                    )
                    adapter.submitList(items)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}