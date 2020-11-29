package com.example.itunesapiapptest.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.itunesapiapptest.R
import com.example.itunesapiapptest.adapters.SongsAdapter
import com.example.itunesapiapptest.adapters.setImage
import com.example.itunesapiapptest.databinding.FragmentAlbumBinding
import com.example.itunesapiapptest.navigation.NavigationFragment
import com.google.android.material.appbar.AppBarLayout
import timber.log.Timber
import kotlin.math.abs

class AlbumFragment : NavigationFragment(){

    lateinit var binding: FragmentAlbumBinding

    private val vm: AlbumViewModel by viewModels()
    private val args: AlbumFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        vm.setLookupId(args.albumId)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songsAdapter = SongsAdapter(
                navigateToPlayer = { trackId, albumId ->
                    findNavController().navigate(
                            AlbumFragmentDirections.actionAlbumFragmentToPlayerFragment(
                                    trackId.toString(), albumId.toString()
                            )
                    )
                }
        )
        
        binding.apply { 
            list.apply { 
                adapter = songsAdapter
                layoutManager = LinearLayoutManager(context)
            }
            
            viewModel = vm
        }
        
        vm.songsData.observe(
                viewLifecycleOwner,
                Observer {data ->
                    data?.let { 
                        songsAdapter.submitList(
                                it
                        )
                    }
                }
        )
        vm.albumData.observe(
                viewLifecycleOwner,
                Observer { data ->
                    data?.let {album ->
                        binding.includeToolbarAlbum.apply {
                            appBarLayout.addOnOffsetChangedListener(
                                AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                                    info.alpha = 1 - abs(verticalOffset).toFloat() / 300
                                }
                            )
                            collapsingToolbarLayout.apply {
                                Glide
                                        .with(albumCover)
                                        .load(album.artworkUrl60)
                                        .apply {
                                            transform(
                                                    CenterCrop(),
                                                    CircleCrop()
                                            )
                                        }
                                        .into(albumCover)

                                artistName.text = album.artistName
                                trackCount.text = getString(R.string.songs_count_template, album.trackCount)
                                toolbar.title = album.collectionName
                                executePendingBindings()
                            }
                        }
                    }
                }
        )
    }
}