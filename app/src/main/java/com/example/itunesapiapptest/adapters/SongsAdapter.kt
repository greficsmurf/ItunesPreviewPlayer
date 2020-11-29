package com.example.itunesapiapptest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Album
import com.example.domain.model.Song
import com.example.itunesapiapptest.databinding.VhDefaultItemBinding
import com.example.itunesapiapptest.model.DefaultViewHolderData

class SongsAdapter(
        private val navigateToPlayer: (
            trackId: Long, albumId: Long?
        ) -> Unit
) : ListAdapter<Song, RecyclerView.ViewHolder>(diffUtil) {
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Song>(){
            override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem.trackId == newItem.trackId
            }
            override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = VhDefaultItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
        )
        return DefaultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is DefaultViewHolder)
            holder.apply {
                bind(
                        DefaultViewHolderData(
                                getItem(position).trackName,
                                getItem(position).artist.artistName
                        )
                )
                setOnClickListener {
                    navigateToPlayer.invoke(
                            getItem(position).trackId,
                            getItem(position).collectionId
                    )
                }
            }
    }
}

