package com.example.mobilebankingapp.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.mobilebankingapp.data.RetrofitRepository
import com.example.mobilebankingapp.model.ExchangeRateResponse

class ApiViewModel (private val retrofitRepository: RetrofitRepository) : ViewModel() {
    val exchangeRateDataState: MutableState<ExchangeRateResponse> = try {
            retrofitRepository.fetchExchangeRates()
        }
        catch (e:Exception) {
            e.printStackTrace()
            throw e
        }
}