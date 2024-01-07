package com.example.mobilebankingapp.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilebankingapp.data.FirebaseRepository
import com.example.mobilebankingapp.data.RetrofitRepository
import com.example.mobilebankingapp.model.ExchangeRateResponse
import com.example.mobilebankingapp.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion

class ApiViewModel (
    private val retrofitRepository: RetrofitRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    val toastMessage= mutableStateOf("")
    private var userId = mutableStateOf("")
    private lateinit var userState: Flow<UserData>
//    var selectedFrom = mutableStateOf("")
//    var selectedTo = mutableStateOf("")
//    var quantity = mutableIntStateOf(0)
//    var resultAmount = mutableIntStateOf(0)
//    var items = mutableListOf<String>()

    val exchangeRateDataState: MutableState<ExchangeRateResponse> = try {
            retrofitRepository.fetchExchangeRates()
        }
        catch (e:Exception) {
            e.printStackTrace()
            throw e
        }
    fun updateUserId(userId: String) {
        this.userId.value = userId
        userState = firebaseRepository.getUserData(userId)
    }
    fun getConversionAmount(amountFrom: Double, currencyFrom: String, currencyTo: String) : Double {
        val ratesFrom = exchangeRateDataState.value.rates[currencyFrom]
        val ratesTo = exchangeRateDataState.value.rates[currencyTo]
        if (ratesTo != null && ratesFrom != null) {
            return (amountFrom*ratesTo/ratesFrom)
        }
        else return -1.0
    }

    fun getConversionAmount(currencyFrom: String, currencyTo: String) : Double {
        val ratesFrom = exchangeRateDataState.value.rates[currencyFrom]
        val ratesTo = exchangeRateDataState.value.rates[currencyTo]
        if (ratesTo != null && ratesFrom != null) {
            return (ratesTo/ratesFrom)
        }
        else return -1.0
    }

    fun submitExchange(dataModel: UserData, amount: Double, currencyFrom: String, currencyTo: String) : Int {
        dataModel.balance[currencyFrom].let {
            if (it != null && it > amount) {
                val resultAmount = getConversionAmount(amount, currencyFrom, currencyTo)
                val remainingBalance = it - amount
                firebaseRepository.updateBalance(currencyFrom, currencyTo, remainingBalance, resultAmount)
                return 1
            }
        }
        return 0
    }
}