package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.data.adapter.CharacterAdapter
import com.example.rickandmorty.data.remote.NetworkUtil
import com.example.rickandmorty.data.remote.NetworkUtil.showCenteredSnackbar
import com.example.rickandmorty.databinding.FragmentHomeBinding

class CharactersFragment : Fragment() {
    private val viewModel by viewModels<CharactersViewModel>()
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

        charactersAdapter = CharacterAdapter { characterId ->
            val bundle = Bundle().apply { putInt("characterId", characterId) }
            view.findNavController().navigate(R.id.fullCharacterFragment, bundle)
        }
        binding.listCharacter.adapter = charactersAdapter
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
        viewModel.characters.observe(viewLifecycleOwner, Observer { characters ->
            charactersAdapter.submitList(characters)
        })
        binding.listCharacter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.loading.value!! &&
                    visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                    firstVisibleItemPosition >= 0) {
                    viewModel.loadNextPage()
                }
            }
        })
        if (!NetworkUtil.isInternetAvailable(requireContext())) {
            showCenteredSnackbar(view)
        } else {
            viewModel.loadCharacters()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}