package com.example.valorantinfo.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.playercards.PlayerCard
import com.example.valorantinfo.repository.PlayerCardRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlayerCardUiState {
    object Loading : PlayerCardUiState()
    data class Success(val playerCard: List<PlayerCard>) : PlayerCardUiState()
    data class Error(val message: String) : PlayerCardUiState()
}

@HiltViewModel
class PlayerCardViewModel @Inject constructor(
    private val playerCardRepository: PlayerCardRepository,
    val application: Application
) : ViewModel() {


    private val _uiStatePlayerCardDetails = MutableStateFlow<PlayerCardDetailsUiState>(PlayerCardDetailsUiState.Loading)
    val uiStatePlayerCardDetails: StateFlow<PlayerCardDetailsUiState> = _uiStatePlayerCardDetails.asStateFlow()

    private val _uiState = MutableStateFlow<PlayerCardUiState>(PlayerCardUiState.Loading)
    val uiState: StateFlow<PlayerCardUiState> = _uiState.asStateFlow()

    private var currentPlayerCards: List<PlayerCard> = emptyList()
    private var prefetchJob: kotlinx.coroutines.Job? = null

    fun loadPlayerCard() {
        viewModelScope.launch {
            try {
                _uiState.value = PlayerCardUiState.Loading
                val playerCard = playerCardRepository.getPlayerCards()
                _uiState.value = PlayerCardUiState.Success(playerCard)
            } catch (e: Exception) {
                _uiState.value = PlayerCardUiState.Error(e.message ?: "Failed to load player card")
            }
        }
    }

    fun loadPlayerCard(uuid: String) {
        viewModelScope.launch {
            try {
                _uiStatePlayerCardDetails.value = PlayerCardDetailsUiState.Loading
                val playerCard = playerCardRepository.getPlayerCard(uuid)
                if (playerCard != null) {
                    _uiStatePlayerCardDetails.value = PlayerCardDetailsUiState.Success(playerCard)
                    // Prefetch the large art image
                    playerCard.largeArt?.let { prefetchImage(it) }
                } else {
                    _uiStatePlayerCardDetails.value = PlayerCardDetailsUiState.Error("Player card not found")
                }
            } catch (e: Exception) {
                _uiStatePlayerCardDetails.value = PlayerCardDetailsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun prefetchNextImages() {
        prefetchJob?.cancel()
        prefetchJob = viewModelScope.launch {
            val nextBatch = currentPlayerCards.take(10)
            prefetchImages(nextBatch)
        }
    }

    private fun prefetchImages(playerCards: List<PlayerCard>) {
        playerCards.forEach { playerCard ->
            playerCard.displayIcon?.let { prefetchImage(it) }
        }
    }

    private fun prefetchImage(url: String) {
        Glide.with(getApplication(application))
            .load(url)
            .preload()
    }

    override fun onCleared() {
        super.onCleared()
        prefetchJob?.cancel()
        // Clear Glide cache when the ViewModel is cleared
        Glide.get(getApplication(application)).clearMemory()
    }
}

sealed class PlayerCardDetailsUiState {
    object Loading : PlayerCardDetailsUiState()
    data class Success(val playerCard: PlayerCard) : PlayerCardDetailsUiState()
    data class Error(val message: String) : PlayerCardDetailsUiState()
}