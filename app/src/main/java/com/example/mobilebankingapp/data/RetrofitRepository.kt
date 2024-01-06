package com.example.mobilebankingapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.mobilebankingapp.model.ExchangeRateResponse
import com.example.mobilebankingapp.model.UserData
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface ExchangeRateApiService {
    @GET("v4/latest/EUR")
    fun getExchangeRates(): Call<ExchangeRateResponse>
}

interface RetrofitRepository {
    fun fetchExchangeRates() : MutableState<ExchangeRateResponse>

}

class NetworkRetrofitRepository : RetrofitRepository {
    private val apiService = ExchangeRateRetrofitInstance.create()

    override fun fetchExchangeRates() : MutableState<ExchangeRateResponse> {
        val call: Call<ExchangeRateResponse> = apiService.getExchangeRates()
        val exchangeRateData: MutableState<ExchangeRateResponse> = mutableStateOf(
            ExchangeRateResponse()
        )

        call.enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse (
                call: Call<ExchangeRateResponse>,
                response: Response<ExchangeRateResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        exchangeRateData.value = it
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                println("Network Error: ${t.message}")
            }
        })
        return exchangeRateData
    }
}

private class ExchangeRateRetrofitInstance {
    companion object {
        private const val EXCHANGE_RATE_URL = "https://api.exchangerate-api.com/"

        fun create(): ExchangeRateApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(EXCHANGE_RATE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ExchangeRateApiService::class.java)
        }
    }
}