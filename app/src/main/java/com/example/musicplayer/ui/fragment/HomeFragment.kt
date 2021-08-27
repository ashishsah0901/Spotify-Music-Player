package com.example.musicplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.example.musicplayer.adapter.OnSongClickListener
import com.example.musicplayer.adapter.SongAdapter
import com.example.musicplayer.data.Song
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.ui.viewmodel.MainViewModel
import com.example.musicplayer.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), OnSongClickListener {

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mainViewModel: MainViewModel

    private lateinit var songAdapter: SongAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songAdapter = SongAdapter(glide, this)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setupRecyclerView()
        subscribeToObservers()

    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when(result.status) {
                Resource.Status.SUCCESS -> {
                    binding.allSongsProgressBar.isVisible = false
                    result.data?.let { songs ->
                        songAdapter.songs = songs
                    }
                }
                Resource.Status.ERROR -> Unit
                Resource.Status.LOADING -> binding.allSongsProgressBar.isVisible = true
            }
        }
    }

    override fun onClickListener(song: Song) {
        mainViewModel.playOrToggleSong(song)
    }
}
