package com.example.mobilebankingapp.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobilebankingapp.AppRepository
import com.example.mobilebankingapp.firebase.CardModel
import com.example.mobilebankingapp.firebase.UserDataModel
import com.example.mobilebankingapp.presentation.login.GoogleAuthClient
import com.example.mobilebankingapp.presentation.login.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class HomeViewModel (private val appRepository: AppRepository) : ViewModel() {
    lateinit var googleAuthUiClient: GoogleAuthClient
    lateinit var userData: UserData

    val userState: Flow<UserDataModel> by lazy { appRepository.getUserData() }

    fun onTransferClick(navController: NavController) {

    }

    fun onAddOrChangeCard(navController: NavController) {

    }

    fun onBuyDifferentCurrency(navController: NavController) {
    }

    fun onHelpClick(navController: NavController) {

    }

    fun onLogout(navController: NavController) {
        viewModelScope.launch {
            navController.navigate("login")
        }
    }

    fun writeDummyDataToFirebase() {
        val userId = googleAuthUiClient.getSignedInUser()?.userId
        val userName = googleAuthUiClient.getSignedInUser()?.username
        val balance = mutableMapOf<String, Int>()
        balance["ron"] = 100
        val card1 = CardModel("Ricu", "0000", "10/10/2023", 254)
        val card2 = CardModel("Ricu", "1111", "10/10/2024", 111)
        val usr = UserDataModel(userName!!, "Ricu", "Alexandru", "01/01/2022", "123456", "10", balance, listOf(card1, card2))
        appRepository.writeToFirebase(usr)
    }
}