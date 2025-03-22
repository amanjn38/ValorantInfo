package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.valorantinfo.data.models.agent.Agent
import com.example.valorantinfo.databinding.ItemAgentBinding

class AgentsAdapter(
    private val onAgentClick: (Agent) -> Unit = {}
) : ListAdapter<Agent, AgentsAdapter.AgentViewHolder>(AgentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentViewHolder {
        val binding = ItemAgentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AgentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AgentViewHolder, position: Int) {
        val agent = getItem(position)
        holder.bind(agent)
    }

    inner class AgentViewHolder(private val binding: ItemAgentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(agent: Agent) {
            binding.apply {
                tvAgentName.text = agent.displayName
                
                // Set agent role if available
                agent.role?.let {
                    tvAgentRole.text = it.displayName
                    
                    // Load role icon
                    Glide.with(itemView)
                        .load(it.displayIcon)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivAgentRole)
                }
                
                // Load agent portrait
                Glide.with(itemView)
                    .load(agent.fullPortrait ?: agent.displayIcon)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivAgentPortrait)
                
                // Set agent background
                agent.background?.let { backgroundUrl ->
                    Glide.with(itemView)
                        .load(backgroundUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivAgentBackground)
                }
                
                // Set click listener
                root.setOnClickListener {
                    onAgentClick(agent)
                }
            }
        }
    }

    class AgentDiffCallback : DiffUtil.ItemCallback<Agent>() {
        override fun areItemsTheSame(oldItem: Agent, newItem: Agent): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Agent, newItem: Agent): Boolean {
            return oldItem == newItem
        }
    }
} 