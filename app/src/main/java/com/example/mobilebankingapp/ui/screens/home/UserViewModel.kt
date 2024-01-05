package com.example.mobilebankingapp.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobilebankingapp.data.FirebaseRepository
import com.example.mobilebankingapp.model.CreditCard
import com.example.mobilebankingapp.model.UserData
import kotlinx.coroutines.flow.Flow


class UserViewModel(private val firebaseRepo: FirebaseRepository) : ViewModel() {
    private var userId = mutableStateOf("")
    lateinit var userState: Flow<UserData>

    fun updateUserId(userId: String) {
        this.userId.value = userId
        userState = firebaseRepo.getUserData(userId)
    }

    fun addCard(creditCard: CreditCard) = firebaseRepo.addCard(creditCard)

    fun deleteCard(cardId: String) = firebaseRepo.deleteCard(cardId)

    fun setDefaultCard(cardId: String, prevCardId: String?) =
        firebaseRepo.setDefaultCard(cardId, prevCardId)
}
