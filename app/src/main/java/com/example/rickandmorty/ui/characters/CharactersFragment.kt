package com.example.rickandmorty.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.data.remote.RickyAndMortyApi
import com.example.rickandmorty.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class CharactersFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: CharacterAdapter
    private val binding get() = _binding!!

    private val viewModel by viewModels<CharactersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = CharacterAdapter()
        val layoutManager = LinearLayoutManager(context)
        binding.listCharacter.layoutManager = layoutManager
        binding.listCharacter.adapter = adapter

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val characterApi = retrofit.create(RickyAndMortyApi::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val list = characterApi.getAllCharacters()
            requireActivity().runOnUiThread {
                binding.apply {
                    adapter.submitList(list.results)
                }
            }
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}