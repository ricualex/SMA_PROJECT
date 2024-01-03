package com.example.mobilebankingapp.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.mobilebankingapp.data.FirebaseRepository
import com.example.mobilebankingapp.model.CreditCard
import com.example.mobilebankingapp.model.UserData
import kotlinx.coroutines.flow.Flow


class HomeViewModel (private val firebaseRepository: FirebaseRepository) : ViewModel() {
//    lateinit var googleAuthUiClient: NetworkGoogleAuthRepository
//    lateinit var userData: UserData

    val userState: Flow<UserData> by lazy { firebaseRepository.getUserData() }

    fun onTransferClick() {

    }

    fun onAddOrChangeCard() {

    }

    fun onBuyDifferentCurrency() {
    }

    fun onHelpClick() {

    }

    fun writeDummyDataToFirebase() {
//        val userId = googleAuthUiClient.getSignedInUser()?.userId
//        val userName = googleAuthUiClient.getSignedInUser()?.username
        val balance = mutableMapOf<String, Int>()
        balance["ron"] = 100
        val card1 = CreditCard("Ricu", "0000", "10/10/2023", 254)
        val card2 = CreditCard("Ricu", "1111", "10/10/2024", 111)
//        val usr = UserDataModel(userName!!, "Ricu", "Alexandru", "01/01/2022", "123456", "10", balance, listOf(card1, card2))
//        appRepository.writeToFirebase(usr)
    }
}