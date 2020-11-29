package com.example.itunesapiapptest.ui.songs

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
import com.example.itunesapiapptest.adapters.SongsAdapter
import com.example.itunesapiapptest.databinding.FragmentSongsBinding
import com.example.itunesapiapptest.navigation.NavigationFragment
import com.example.itunesapiapptest.navigation.navigateSafe

class SongsFragment : NavigationFragment(){

    private val vm: SongsViewModel by viewModels()
    lateinit var binding: FragmentSongsBinding
    private val args: SongsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSongsBinding.inflate(
                inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.setSearchTerm(args.searchTerm)

        if(!args.searchTerm.isNullOrBlank()){
            binding.includeToolbar.toolbar.title = getString(R.string.title_songs_fragment_template, args.searchTerm)
        }

        val songsAdapter = SongsAdapter(
                navigateToPlayer = { trackId, albumId ->
                    findNavController().navigateSafe(
                            SongsFragmentDirections.actionSongsFragmentToPlayerFragment(
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

            lifecycleOwner = viewLifecycleOwner
        }

        vm.songsData.observe(viewLifecycleOwner, Observer {
            it?.let {
                songsAdapter.submitList(it)
            }
        })
    }
}