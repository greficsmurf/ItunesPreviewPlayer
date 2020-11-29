package com.example.itunesapiapptest.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Artist
import com.example.itunesapiapptest.R
import com.example.itunesapiapptest.adapters.AlbumsAdapter
import com.example.itunesapiapptest.adapters.ArtistAdapter
import com.example.itunesapiapptest.adapters.SongsAdapter
import com.example.itunesapiapptest.adapters.setVisibility
import com.example.itunesapiapptest.databinding.FragmentHomeBinding
import com.example.itunesapiapptest.navigation.NavigationFragment
import com.example.itunesapiapptest.navigation.navigateSafe
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.concurrent.timerTask

class HomeFragment : NavigationFragment(){

    lateinit var binding: FragmentHomeBinding

    private val vm: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchToolbar()
        val albumsAdapter = AlbumsAdapter(
                navigateToAlbumSongs = { id ->
                    findNavController().navigateSafe(
                            HomeFragmentDirections.actionHomeFragmentToAlbumFragment(
                                    id.toString()
                            )
                    )
                }
        )

        val artistAdapter = ArtistAdapter()
        val songsAdapter = SongsAdapter(
                navigateToPlayer = {trackId, albumId ->
                    findNavController().navigateSafe(
                            HomeFragmentDirections.actionHomeFragmentToPlayerFragment(
                                    trackId.toString(), albumId.toString()
                            )
                    )
                }
        )
        binding.apply {
            albumsList.apply {
                adapter = albumsAdapter
                layoutManager = LinearLayoutManager(context)
            }
            songsList.apply {
                adapter = songsAdapter
                layoutManager = LinearLayoutManager(context)
            }
            artistsList.apply {
                adapter = artistAdapter
                layoutManager = LinearLayoutManager(context)
            }
            lifecycleOwner = viewLifecycleOwner
            viewModel = vm
        }

        vm.albumsData.observe(viewLifecycleOwner, Observer {data ->
            setVisibility(binding.albumsSectionTitle, !data.isNullOrEmpty())
            data?.let {
                albumsAdapter.submitList(data)
            }
        })
        vm.songsData.observe(viewLifecycleOwner, Observer {data ->
            setVisibility(binding.songsSectionTitle, !data.isNullOrEmpty())
            data?.let {
                songsAdapter.submitList(data)
            }
        })
        vm.artistData.observe(viewLifecycleOwner, Observer {data ->
            setVisibility(binding.artistsSectionTitle, !data.isNullOrEmpty())
            data?.let {
                artistAdapter.submitList(data)
            }
        })

        vm.navigateToAllAlbums.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAlbumsFragment(vm.searchText.value ?: ""))
            }
        })
        vm.navigateToAllSongs.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSongsFragment(
                                null,
                                vm.searchText.value
                        )
                )
            }
        })
    }

    private fun setUpSearchToolbar(){
        binding.includeToolbarHome.searchEditText.apply {
            try {
                setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        vm.setSearchText(text.toString())
                        true
                    }
                    else
                        false
                }
                addTextChangedListener(
                    object : TextWatcher {
                        private var timer = Timer()
                        private val DELAY = 700L

                        override fun afterTextChanged(s: Editable?) {
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            timer.cancel()

                            if(s.isNullOrBlank())
                            {
                                vm.setSearchText(s.toString())
                            }

                            timer = Timer()
                            timer.schedule(
                                timerTask {
                                    vm.setSearchText(s.toString())
                                },
                                DELAY
                            )
                        }

                    }
                )
                isSingleLine = true
//                    width = Int.MAX_VALUE
                requestFocus()
//                toggleKeyBoard()
                setText(vm.searchText.value)
                selectAll()
            }catch (e: Exception){
                Snackbar.make(requireView(), getString(R.string.search_bar_init_error), Snackbar.LENGTH_LONG).show()
            }
        }
    }



}