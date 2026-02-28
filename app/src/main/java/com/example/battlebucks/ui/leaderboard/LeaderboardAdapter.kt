package com.example.battlebucks.ui.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.battlebucks.databinding.ItemLeaderboardBinding
import com.example.battlebucks.model.LeaderboardItem


class LeaderboardAdapter :
    ListAdapter<LeaderboardItem, LeaderboardAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaderboardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemLeaderboardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LeaderboardItem) {

            binding.tvRank.text = item.rank.toString()
            binding.tvUsername.text = item.username
            binding.tvScore.text = item.score.toString()

            if (item.isUpdated) {
                animateUpdate()
            }
        }

        private fun animateUpdate() {
            binding.root.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(150)
                .withEndAction {
                    binding.root.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }
                .start()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LeaderboardItem>() {
        override fun areItemsTheSame(
            oldItem: LeaderboardItem,
            newItem: LeaderboardItem
        ): Boolean = oldItem.playerId == newItem.playerId

        override fun areContentsTheSame(
            oldItem: LeaderboardItem,
            newItem: LeaderboardItem
        ): Boolean = oldItem == newItem
    }
}