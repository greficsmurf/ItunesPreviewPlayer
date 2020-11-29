package com.example.itunesapiapptest.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.itunesapiapptest.R
import com.example.itunesapiapptest.adapters.AlbumsAdapter
import com.example.itunesapiapptest.databinding.FragmentAlbumsBinding
import com.example.itunesapiapptest.navigation.NavigationFragment
import com.example.itunesapiapptest.navigation.navigateSafe
import kotlinx.android.synthetic.main.include_layout_default_recycler.view.*


class AlbumsFragment : NavigationFragment(){

    private lateinit var binding: FragmentAlbumsBinding

    private val vm: AlbumsViewModel by viewModels()

    private val args: AlbumsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlbumsBinding.inflate(
                inflater, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.setSearchText(args.searchTerm)
        binding.includeToolbar.toolbar.title = getString(R.string.title_albums_fragment_template, args.searchTerm)
        val albumsAdapter = AlbumsAdapter(
                navigateToAlbumSongs = {id ->
                    findNavController().navigateSafe(
                            AlbumsFragmentDirections.actionAlbumsFragmentToAlbumFragment(
                                    id.toString()
                            )
                    )
                }
        )
        binding.apply {
            list.apply {
                adapter = albumsAdapter
                layoutManager = LinearLayoutManager(context)
            }
            viewModel = vm
        }

        vm.data.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                albumsAdapter.submitList(it)
            }
        })
    }
}