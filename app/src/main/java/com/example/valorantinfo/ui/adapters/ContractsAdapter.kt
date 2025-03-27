package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.contracts.Data
import com.example.valorantinfo.databinding.ItemContractBinding

class ContractsAdapter(
    private val onContractClick: (Data) -> Unit,
) : ListAdapter<Data, ContractsAdapter.ViewHolder>(ContractDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContractBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemContractBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contract: Data) {
            binding.apply {
                contractName.text = contract.displayName
                contractDescription.text = contract.content.relationType

                // Load contract image
                Glide.with(contractImage)
                    .load(contract.displayIcon)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(contractImage)
            }

            itemView.setOnClickListener { onContractClick(contract) }
        }
    }

    private class ContractDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }
}
