package com.example.itunesapiapptest.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.itunesapiapptest.databinding.VhDefaultItemBinding
import com.example.itunesapiapptest.model.DefaultViewHolderData

open class DefaultViewHolder(
        private val binding: VhDefaultItemBinding,
        private val isNavigable: Boolean = true
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: DefaultViewHolderData){
        binding.apply {
            text.apply {
                text = data.title
            }
            description.apply {
                text = data.description
                emptyGone(this, data.description)
            }
            setVisibility(navArrow, isNavigable)
            if(!isNavigable)
                layout.background = null
            executePendingBindings()
        }
    }

    fun setOnClickListener(
            callback: () -> Unit
    ){
        binding.layout.setOnClickListener {
            callback.invoke()
        }
        binding.executePendingBindings()
    }
}