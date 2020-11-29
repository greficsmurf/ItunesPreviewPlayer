package com.example.itunesapiapptest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Album
import com.example.itunesapiapptest.databinding.VhDefaultItemBinding
import com.example.itunesapiapptest.model.DefaultViewHolderData

//should be repalfec
class AlbumsAdapter(
        private val navigateToAlbumSongs: (albumId: Long) -> Unit
) : ListAdapter<Album, RecyclerView.ViewHolder>(diffUtil) {
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Album>(){
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.artistId == newItem.artistId
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
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
                                getItem(position).collectionName,
                                getItem(position).artistName
                        )
                )
                setOnClickListener {
                    navigateToAlbumSongs.invoke(getItem(position).collectionId)
                }
            }
    }
}
