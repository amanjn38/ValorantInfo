package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.levelborder.LevelBorder
import com.example.valorantinfo.databinding.ItemLevelBorderBinding

class LevelBorderAdapter(
    private val onItemClick: (LevelBorder) -> Unit
) : ListAdapter<LevelBorder, LevelBorderAdapter.LevelBorderViewHolder>(LevelBorderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelBorderViewHolder {
        val binding = ItemLevelBorderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LevelBorderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LevelBorderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LevelBorderViewHolder(
        private val binding: ItemLevelBorderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(levelBorder: LevelBorder) {
            binding.apply {
                nameTextView.text = levelBorder.displayName
                levelTextView.text = "Level ${levelBorder.startingLevel}"
                
                Glide.with(binding.root)
                    .load(levelBorder.smallPlayerCardAppearance)
                    .into(borderImageView)
            }
        }
    }

    private class LevelBorderDiffCallback : DiffUtil.ItemCallback<LevelBorder>() {
        override fun areItemsTheSame(oldItem: LevelBorder, newItem: LevelBorder): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: LevelBorder, newItem: LevelBorder): Boolean {
            return oldItem == newItem
        }
    }
} 