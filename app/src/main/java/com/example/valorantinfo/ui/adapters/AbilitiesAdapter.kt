package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.agentDetails.Ability
import com.example.valorantinfo.databinding.ItemAbilityBinding

class AbilitiesAdapter : ListAdapter<Ability, AbilitiesAdapter.AbilityViewHolder>(AbilityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilityViewHolder {
        val binding = ItemAbilityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return AbilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AbilityViewHolder, position: Int) {
        val ability = getItem(position)
        holder.bind(ability)
    }

    inner class AbilityViewHolder(private val binding: ItemAbilityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ability: Ability) {
            binding.apply {
                tvAbilityName.text = ability.displayName
                tvAbilityDescription.text = ability.description

                // Load ability icon
                ability.displayIcon?.let { iconUrl ->
                    Glide.with(itemView.context)
                        .load(iconUrl)
                        .placeholder(R.drawable.ic_agent_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivAbilityIcon)
                }
            }
        }
    }

    class AbilityDiffCallback : DiffUtil.ItemCallback<Ability>() {
        override fun areItemsTheSame(oldItem: Ability, newItem: Ability): Boolean {
            return oldItem.slot == newItem.slot && oldItem.displayName == newItem.displayName
        }

        override fun areContentsTheSame(oldItem: Ability, newItem: Ability): Boolean {
            return oldItem == newItem
        }
    }
}
