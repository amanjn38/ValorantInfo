package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.valorantinfo.data.models.buddy.BuddyLevel
import com.example.valorantinfo.databinding.ItemBuddyLevelBinding

class BuddyLevelsAdapter(
    val onLevelClick: ((BuddyLevel) -> Unit)? = null,
) : ListAdapter<BuddyLevel, BuddyLevelsAdapter.LevelViewHolder>(LevelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val binding = ItemBuddyLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return LevelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LevelViewHolder(private val binding: ItemBuddyLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(level: BuddyLevel) {
            // Set level name
            binding.tvLevelName.text = level.displayName.uppercase()

            // Set charm level text
            binding.tvCharmLevel.text = "CHARM LEVEL: ${level.charmLevel}"

            // Load level icon
            Glide.with(binding.ivLevelIcon)
                .load(level.displayIcon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivLevelIcon)

            // Set click listener if provided
            if (onLevelClick != null) {
                binding.root.setOnClickListener {
                    onLevelClick.invoke(level)
                }
                binding.root.isClickable = true
                binding.root.isFocusable = true
            }
        }
    }

    class LevelDiffCallback : DiffUtil.ItemCallback<BuddyLevel>() {
        override fun areItemsTheSame(oldItem: BuddyLevel, newItem: BuddyLevel): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: BuddyLevel, newItem: BuddyLevel): Boolean {
            return oldItem == newItem
        }
    }
}
