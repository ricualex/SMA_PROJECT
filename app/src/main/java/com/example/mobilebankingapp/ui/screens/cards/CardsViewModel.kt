package com.example.mobilebankingapp.ui.screens.cards

import androidx.lifecycle.ViewModel
import com.example.mobilebankingapp.ui.screens.cards.CardsScreenState
import com.example.mobilebankingapp.model.CreditCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CardsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CardsScreenState())
    val uiState = _uiState.asStateFlow()

    fun setCards(cards: List<CreditCard>) {
        _uiState.update { CardsScreenState(cards) }
    }
}