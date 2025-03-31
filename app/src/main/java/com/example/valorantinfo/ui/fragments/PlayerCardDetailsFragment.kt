package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.valorantinfo.R
import com.example.valorantinfo.ui.viewmodels.PlayerCardDetailsUiState
import com.example.valorantinfo.ui.viewmodels.PlayerCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerCardDetailsFragment : Fragment() {

    private val viewModel: PlayerCardViewModel by viewModels()
    private val args: PlayerCardDetailsFragmentArgs by navArgs()

    private lateinit var cardImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    private val glideOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .placeholder(android.R.color.darker_gray)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        observeUiState()
        viewModel.loadPlayerCard(args.playerCardUuid)
    }

    private fun setupViews(view: View) {
        cardImageView = view.findViewById(R.id.cardImageView)
        nameTextView = view.findViewById(R.id.nameTextView)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorTextView)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStatePlayerCardDetails.collectLatest { state ->
                when (state) {
                    is PlayerCardDetailsUiState.Loading -> {
                        showLoading()
                    }

                    is PlayerCardDetailsUiState.Success -> {
                        showSuccess(state)
                    }

                    is PlayerCardDetailsUiState.Error -> {
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        cardImageView.visibility = View.GONE
        nameTextView.visibility = View.GONE
        errorTextView.visibility = View.GONE
    }

    private fun showSuccess(state: PlayerCardDetailsUiState.Success) {
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.GONE

        val playerCard = state.playerCard

        // Load image using Glide with optimized options
        playerCard.largeArt?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .apply(glideOptions)
                .into(cardImageView)
            cardImageView.visibility = View.VISIBLE
        }
        nameTextView.text = playerCard.displayName
        nameTextView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        cardImageView.visibility = View.GONE
        nameTextView.visibility = View.GONE
        errorTextView.text = message
        errorTextView.visibility = View.VISIBLE
    }
}