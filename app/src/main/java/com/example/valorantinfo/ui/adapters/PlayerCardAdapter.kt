package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.valorantinfo.data.models.playercards.PlayerCard
import com.example.valorantinfo.databinding.ItemPlayerCardBinding

class PlayerCardAdapter(
    private val onPlayerCardClick: (PlayerCard) -> Unit
) : ListAdapter<PlayerCard, PlayerCardAdapter.PlayerCardViewHolder>(PlayerCardDiffCallback()) {

    private val glideOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .placeholder(android.R.color.darker_gray)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerCardViewHolder {
        val binding = ItemPlayerCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlayerCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerCardAdapter.PlayerCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlayerCardViewHolder(
        private val binding: ItemPlayerCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPlayerCardClick(getItem(position))
                }
            }
        }

        fun bind(playerCard: PlayerCard) {
            binding.apply {
                nameTextView.text = playerCard.displayName
                
                playerCard.displayIcon?.let { imageUrl ->
                    Glide.with(cardImageView)
                        .load(imageUrl)
                        .apply(glideOptions)
                        .into(cardImageView)
                }
            }
        }
    }

    private class PlayerCardDiffCallback : DiffUtil.ItemCallback<PlayerCard>() {
        override fun areItemsTheSame(oldItem: PlayerCard, newItem: PlayerCard): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: PlayerCard, newItem: PlayerCard): Boolean {
            return oldItem == newItem
        }
    }
} 