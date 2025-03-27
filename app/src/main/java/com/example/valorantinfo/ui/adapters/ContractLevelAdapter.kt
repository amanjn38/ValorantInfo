package com.example.valorantinfo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.contracts.Level
import com.example.valorantinfo.data.models.contracts.RewardType

class ContractLevelAdapter : ListAdapter<Level, ContractLevelAdapter.LevelViewHolder>(LevelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contract_level, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val levelNumber: TextView = itemView.findViewById(R.id.levelNumber)
        private val levelProgress: TextView = itemView.findViewById(R.id.levelProgress)
        private val xpReward: TextView = itemView.findViewById(R.id.xpReward)
        private val vpReward: TextView = itemView.findViewById(R.id.vpReward)
        private val skinReward: TextView = itemView.findViewById(R.id.skinReward)
        private val sprayReward: TextView = itemView.findViewById(R.id.sprayReward)
        private val titleReward: TextView = itemView.findViewById(R.id.titleReward)
        private val buddyReward: TextView = itemView.findViewById(R.id.buddyReward)

        private val xpRewardCard: View = itemView.findViewById(R.id.xpRewardCard)
        private val vpRewardCard: View = itemView.findViewById(R.id.vpRewardCard)
        private val skinRewardCard: View = itemView.findViewById(R.id.skinRewardCard)
        private val sprayRewardCard: View = itemView.findViewById(R.id.sprayRewardCard)
        private val titleRewardCard: View = itemView.findViewById(R.id.titleRewardCard)
        private val buddyRewardCard: View = itemView.findViewById(R.id.buddyRewardCard)

        fun bind(level: Level) {
            levelNumber.text = "Level ${adapterPosition + 1}"
            levelProgress.text = "${level.xp} XP / ${level.xp} XP"

            // Hide all reward cards initially
            xpRewardCard.visibility = View.GONE
            vpRewardCard.visibility = View.GONE
            skinRewardCard.visibility = View.GONE
            sprayRewardCard.visibility = View.GONE
            titleRewardCard.visibility = View.GONE
            buddyRewardCard.visibility = View.GONE

            // Show and set reward based on type
            val rewardType = RewardType.fromString(level.reward.type)
            when (rewardType) {
                RewardType.XP -> {
                    xpRewardCard.visibility = View.VISIBLE
                    xpReward.text = "${level.reward.amount} XP"
                }
                RewardType.VP -> {
                    vpRewardCard.visibility = View.VISIBLE
                    vpReward.text = "${level.reward.amount} VP"
                }
                RewardType.SKIN -> {
                    skinRewardCard.visibility = View.VISIBLE
                    skinReward.text = "Skin"
                }
                RewardType.SPRAY -> {
                    sprayRewardCard.visibility = View.VISIBLE
                    sprayReward.text = "Spray"
                }
                RewardType.TITLE -> {
                    titleRewardCard.visibility = View.VISIBLE
                    titleReward.text = "Title"
                }
                RewardType.BUDDY -> {
                    buddyRewardCard.visibility = View.VISIBLE
                    buddyReward.text = "Buddy"
                }
            }
        }
    }

    private class LevelDiffCallback : DiffUtil.ItemCallback<Level>() {
        override fun areItemsTheSame(oldItem: Level, newItem: Level): Boolean {
            return oldItem.xp == newItem.xp && oldItem.vpCost == newItem.vpCost
        }

        override fun areContentsTheSame(oldItem: Level, newItem: Level): Boolean {
            return oldItem == newItem
        }
    }
}
