package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.gamemodes.GameModeEquippable
import com.example.valorantinfo.databinding.ItemEquippableBinding

class EquippableAdapter : ListAdapter<GameModeEquippable, EquippableAdapter.EquippableViewHolder>(EquippableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquippableViewHolder {
        val binding = ItemEquippableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EquippableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EquippableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EquippableViewHolder(
        private val binding: ItemEquippableBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(equippable: GameModeEquippable) {
            binding.apply {
                tvEquippableName.text = equippable.displayName

                Glide.with(ivEquippableIcon)
                    .load(equippable.displayIcon)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(ivEquippableIcon)
            }
        }
    }

    private class EquippableDiffCallback : DiffUtil.ItemCallback<GameModeEquippable>() {
        override fun areItemsTheSame(oldItem: GameModeEquippable, newItem: GameModeEquippable): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: GameModeEquippable, newItem: GameModeEquippable): Boolean {
            return oldItem == newItem
        }
    }
} 