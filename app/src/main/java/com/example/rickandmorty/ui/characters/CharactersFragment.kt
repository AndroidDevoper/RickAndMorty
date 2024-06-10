package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.data.adapter.CharacterAdapter
import com.example.rickandmorty.data.remote.NetworkUtil
import com.example.rickandmorty.data.remote.NetworkUtil.showCenteredSnackbar
import com.example.rickandmorty.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

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

        // Инициализация адаптера и RecyclerView
        charactersAdapter = CharacterAdapter()
        binding.listCharacter.adapter = charactersAdapter

        // Наблюдение за состоянием загрузки
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Наблюдение за списком персонажей
        viewModel.characters.observe(viewLifecycleOwner, Observer { characters ->
            charactersAdapter.submitList(characters)
        })

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                showCenteredSnackbar(view, it)
                viewModel.clearError()
            }
        })

        // Установка слушателя для загрузки следующей страницы при достижении конца списка
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

        // Проверка интернет-соединения
        if (!NetworkUtil.isInternetAvailable(requireContext())) {
            showCenteredSnackbar(view, "Нет интернета или данные не доступны")
        } else {
            viewModel.loadCharacters()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}