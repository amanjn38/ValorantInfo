package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.flex.Flex
import com.example.valorantinfo.databinding.ItemFlexBinding

class FlexAdapter(
    private val onFlexClick: (Flex) -> Unit
) : ListAdapter<Flex, FlexAdapter.FlexViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexViewHolder {
        val binding = ItemFlexBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FlexViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlexViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FlexViewHolder(
        private val binding: ItemFlexBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFlexClick(getItem(position))
                }
            }
        }

        fun bind(flex: Flex) {
            binding.apply {
                tvFlexName.text = flex.displayName
                tvFlexShortName.text = flex.displayNameAllCaps
                tvAssetPath.text = flex.assetPath

                Glide.with(ivFlexIcon)
                    .load(flex.displayIcon)
                    .into(ivFlexIcon)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Flex>() {
        override fun areItemsTheSame(oldItem: Flex, newItem: Flex): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Flex, newItem: Flex): Boolean {
            return oldItem == newItem
        }
    }
} 