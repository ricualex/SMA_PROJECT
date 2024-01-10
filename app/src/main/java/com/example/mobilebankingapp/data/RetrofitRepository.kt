package com.example.mobilebankingapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.mobilebankingapp.model.ExchangeRateResponse
import com.example.mobilebankingapp.model.GoogleMapsApiResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApiService {
    @GET("v4/latest/EUR")
    fun getExchangeRates(): Call<ExchangeRateResponse>
}

interface GoogleMapsApiService {
    @GET("maps/api/place/nearbysearch/json")
    fun getNearbyAtms(
        @Query("location") location: String,
        @Query("radius") radius: Int = 5000,
        @Query("type") type: String = "atm",
        @Query("key") key: String = "AIzaSyDvBOWr51gOKB12B3yt_fg3714EdSTCccE"

    ): Call<Any>
}

interface RetrofitRepository {
    fun fetchExchangeRates(): MutableState<ExchangeRateResponse>
    fun getNearbyAtms(location: String, callback: (List<GoogleMapsApiResponse>) -> Unit)

}

class NetworkRetrofitRepository : RetrofitRepository {
    private val apiService = ExchangeRateRetrofitInstance.create()
    private val mapsApiService = GoogleMapsRetrofitInstance.create()

    override fun fetchExchangeRates(): MutableState<ExchangeRateResponse> {
        val call: Call<ExchangeRateResponse> = apiService.getExchangeRates()
        val exchangeRateData: MutableState<ExchangeRateResponse> = mutableStateOf(
            ExchangeRateResponse()
        )

        call.enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(
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

    override fun getNearbyAtms(
        location: String,
        callback: (List<GoogleMapsApiResponse>) -> Unit
    ) {
        val call: Call<Any> = mapsApiService.getNearbyAtms(location)
        call.enqueue(object : Callback<Any> {
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val jsonString = Gson().toJson(response.body())
                        val jsonObject = JsonParser().parse(jsonString).asJsonObject
                        val resultsArray = jsonObject.getAsJsonArray("results")
                        resultsArray.map { resultItem ->
                            val name =
                                resultItem.asJsonObject.getAsJsonPrimitive("name")?.asString ?: ""
                            val location = resultItem.asJsonObject.getAsJsonObject("geometry")
                                .getAsJsonObject("location")
                            val lat = location?.getAsJsonPrimitive("lat")?.asDouble ?: 0.0
                            val lng = location?.getAsJsonPrimitive("lng")?.asDouble ?: 0.0
                            GoogleMapsApiResponse(
                                name = name,
                                location = listOf(mapOf("lat" to lat, "lng" to lng))
                            )
                        }.let {
                            callback(it)
                        }
                    }
                } else {
                    callback(emptyList())
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                callback(emptyList())
                println("Network Error: ${t.message}")
            }
        })
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

private class GoogleMapsRetrofitInstance {
    companion object {
        private const val GOOGLE_MAPS_URL = "https://maps.googleapis.com/"

        fun create(): GoogleMapsApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(GOOGLE_MAPS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(GoogleMapsApiService::class.java)
        }
    }
}