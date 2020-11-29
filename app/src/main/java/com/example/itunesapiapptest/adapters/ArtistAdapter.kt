package com.example.itunesapiapptest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Album
import com.example.domain.model.Artist
import com.example.itunesapiapptest.databinding.VhDefaultItemBinding
import com.example.itunesapiapptest.model.DefaultViewHolderData

class ArtistAdapter : ListAdapter<Artist, RecyclerView.ViewHolder>(diffUtil) {
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Artist>(){
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem.artistId == newItem.artistId
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = VhDefaultItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
        )
        return DefaultViewHolder(binding, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is DefaultViewHolder)
            holder.bind(
                    DefaultViewHolderData(
                            getItem(position).artistName
                    )
            )
    }
}
