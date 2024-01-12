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
    lateinit var registrationData: UserData

    fun resetState() {
        registrationData = UserData()
    }

    fun submitRegister(firstName: String, lastName: String, cnp: String, birthDate: String, keyStoreKey: String) {
        registrationData = UserData(firstName = firstName, lastName = lastName, cnp = cnp, birthDate = birthDate, balance = mapOf("RON" to 0.0))
        firebaseRepo.registerUser(registrationData.encrypt(keyStoreKey))

    }

    fun updateUserId(userId: String) {
        this.userId.value = userId
        userState = firebaseRepo.getUserData(userId)
    }

    fun addCard(creditCard: CreditCard, keyStoreKey: String) =
        firebaseRepo.addCard(creditCard, keyStoreKey)

    fun deleteCard(cardId: String) = firebaseRepo.deleteCard(cardId)

    fun setDefaultCard(cardId: String, prevCardId: String?) =
        firebaseRepo.setDefaultCard(cardId, prevCardId)
}
