package com.example.itunesapiapptest.ui.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.itunesapiapptest.MainActivity
import com.example.itunesapiapptest.R
import com.example.itunesapiapptest.adapters.setImage
import com.example.itunesapiapptest.databinding.FragmentPlayerBinding
import com.example.itunesapiapptest.navigation.NavigationFragment
import com.example.itunesapiapptest.services.PlayerService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.io.IOException
import java.lang.Exception

class PlayerFragment : NavigationFragment(){

    companion object{
        const val DELAY_MILLIS = 1000L
    }

    private lateinit var playerService: PlayerService
    private lateinit var connection: ServiceConnection
    private var isPlayerBound = false

    lateinit var binding: FragmentPlayerBinding
    private var shouldRefreshSeekbar = true

    private val args: PlayerFragmentArgs by navArgs()
    private val vm: PlayerViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPlayerService(requireContext())
    }
    private fun initPlayerService(context: Context){
        connection = object : ServiceConnection{
            override fun onServiceDisconnected(name: ComponentName?) {
                isPlayerBound = false
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as PlayerService.PlayerBinder
                playerService = binder.getService()

                isPlayerBound = true

                initServiceBindings()
            }
        }
        Intent(context, PlayerService::class.java).also {intent ->

            (activity as MainActivity).apply {
                if(!PlayerService.isRunning)
                    startService(intent)
                bindService(intent, connection, Context.BIND_IMPORTANT)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlayerBinding.inflate(
                inflater, container, false
        )

        binding.apply {
            viewModel = vm
            lifecycleOwner = viewLifecycleOwner
        }

        vm.setData(args.songId, args.albumId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            playButton.setOnClickListener {
                if(isPlayerBound)
                    playerService.playPauseSong()
            }
            nextButton.setOnClickListener {
                if(isPlayerBound)
                    playerService.nextSong()
            }
            prevButton.setOnClickListener {
                if(isPlayerBound)
                    playerService.prevSong()
            }
        }
        initSeekBar()

        vm.songs.observe(viewLifecycleOwner, Observer { songList ->
            songList?.let {songs ->
                playerService.setSongs(songs, vm.selectedSongIndex)
            }
        })

        vm.currentPlayerPos.observe(viewLifecycleOwner, Observer {
            binding.apply {
                seekBar.apply {
                    max = playerService.duration
                    progress = it
                }
            }
        })


        binding.seekBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if(fromUser && isPlayerBound)
                            playerService.seekTo(progress)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        shouldRefreshSeekbar = false
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        shouldRefreshSeekbar = true
                    }
                }
        )


    }

    private fun initSeekBar(){
        lifecycleScope.launch(Dispatchers.Default) {
            while (true){
                if(shouldRefreshSeekbar && isPlayerBound)
                    vm.setPlayerPos(playerService.songPos)
                delay(DELAY_MILLIS)
            }
        }
    }

    private fun initServiceBindings(){
        if(isPlayerBound)
        {
            lifecycleScope.launch {
                playerService.currentSong.collect { song ->
                    song?.let {
                        binding.includeToolbar.toolbar.title = song.trackName
                        Glide
                                .with(binding.coverImage)
                                .load(song.artWorkUrl100)
                                .into(binding.coverImage)
                    }
                }
            }
            lifecycleScope.launch {
                playerService.isPlaying.collect {isPlaying ->
                    vm.setPlayingStatus(isPlaying)
                }
            }
            lifecycleScope.launch {
                playerService.isPrepared.collect {isPrepared ->
                    vm.setPreparedStatus(isPrepared)
                }
            }
            lifecycleScope.launch {
                playerService.isLeftEnd.collect {
                    vm.setLeftEnd(it)
                }
            }
            lifecycleScope.launch {
                playerService.isRightEnd.collect {
                    vm.setRightEnd(it)
                }
            }
            lifecycleScope.launch {
                playerService.isDestroyed.collect{
                    if(it)
                        findNavController().popBackStack()
                }
            }
        }
    }

}