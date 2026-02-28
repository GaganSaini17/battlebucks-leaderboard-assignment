package com.example.battlebucks.ui.leaderboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.battlebucks.databinding.ActivityLeaderboardBinding
import kotlinx.coroutines.launch

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderboardBinding
    private val viewModel: LeaderboardViewModel by viewModels()
    private val adapter = LeaderboardAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            val totalScroll = appBarLayout.totalScrollRange
            val progress = kotlin.math.abs(verticalOffset).toFloat() / totalScroll

            val smoothProgress = FastOutSlowInInterpolator().getInterpolation(progress)

            // --- SCALE ---
            val scale = 1f - (0.3f * smoothProgress)
            binding.llContent.scaleX = scale
            binding.llContent.scaleY = scale
            binding.ivIcon.scaleX = scale
            binding.ivIcon.scaleY = scale

            // --- ICON MOVE LEFT ---
            // from 0.5 → 0.075
            val iconStartV = 0.5f
            val iconEndV = 0.075f
            val iconPercentV = iconStartV - (iconStartV - iconEndV) * smoothProgress
            binding.glIconV.setGuidelinePercent(iconPercentV)

            // --- ICON MOVE CENTER ---
            // from 0.25 → 0.5
            val iconStart = 0.25f
            val iconEnd = 0.5f
            val iconPercent = iconStart - (iconStart - iconEnd) * smoothProgress
            binding.glIconH.setGuidelinePercent(iconPercent)

            // --- TEXT MOVE LEFT ---
            // from 0.5 → 0.35
            val textStartV = 0.5f
            val textEndV = 0.35f
            val textPercentV = textStartV - (textStartV - textEndV) * smoothProgress
            binding.glTextV.setGuidelinePercent(textPercentV)

            // --- TEXT MOVE UP ---
            // from 0.75 → 0.5
            val textStart = 0.75f
            val textEnd = 0.5f
            val textPercent = textStart - (textStart - textEnd) * smoothProgress
            binding.glTextH.setGuidelinePercent(textPercent)
        }

        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->

                    // Update List
                    adapter.submitList(state.leaderboard)

                    // Update Header
                    state.currentUserRank?.let {
                        binding.tvYourRank.text = "Your Rank: $it"
                    }

                    state.currentUserScore?.let {
                        binding.tvYourScore.text = "Your Score: $it"
                    }
                }
            }
        }
    }
}