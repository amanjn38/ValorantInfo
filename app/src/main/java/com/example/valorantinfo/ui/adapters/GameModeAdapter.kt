package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.databinding.ItemGameModeBinding

class GameModeAdapter(
    private val onGameModeClick: (GameMode) -> Unit
) : ListAdapter<GameMode, GameModeAdapter.GameModeViewHolder>(GameModeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModeViewHolder {
        val binding = ItemGameModeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameModeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameModeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GameModeViewHolder(
        private val binding: ItemGameModeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onGameModeClick(getItem(position))
                }
            }
        }

        fun bind(gameMode: GameMode) {
            binding.apply {
                tvGameModeName.text = gameMode.displayName
                tvGameModeDescription.text = gameMode.description
                tvGameModeDuration.text = gameMode.duration

                Glide.with(ivGameModeIcon)
                    .load(gameMode.displayIcon)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(ivGameModeIcon)
            }
        }
    }

    private class GameModeDiffCallback : DiffUtil.ItemCallback<GameMode>() {
        override fun areItemsTheSame(oldItem: GameMode, newItem: GameMode): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: GameMode, newItem: GameMode): Boolean {
            return oldItem == newItem
        }
    }
} 