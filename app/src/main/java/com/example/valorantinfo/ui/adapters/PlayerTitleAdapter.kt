package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantinfo.data.models.playertitles.PlayerTitle
import com.example.valorantinfo.databinding.ItemPlayerTitleBinding

class PlayerTitleAdapter(
    private val onPlayerTitleClick: (PlayerTitle) -> Unit
) : ListAdapter<PlayerTitle, PlayerTitleAdapter.PlayerTitleViewHolder>(PlayerTitleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerTitleViewHolder {
        val binding = ItemPlayerTitleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerTitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerTitleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlayerTitleViewHolder(
        private val binding: ItemPlayerTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPlayerTitleClick(getItem(position))
                }
            }
        }

        fun bind(playerTitle: PlayerTitle) {
            binding.apply {
                titleTextView.text = playerTitle.titleText
                displayNameTextView.text = playerTitle.displayName
            }
        }
    }

    private class PlayerTitleDiffCallback : DiffUtil.ItemCallback<PlayerTitle>() {
        override fun areItemsTheSame(oldItem: PlayerTitle, newItem: PlayerTitle): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: PlayerTitle, newItem: PlayerTitle): Boolean {
            return oldItem == newItem
        }
    }
} 