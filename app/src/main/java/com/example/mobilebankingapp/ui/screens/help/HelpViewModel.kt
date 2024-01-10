package com.example.mobilebankingapp.ui.screens.help

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilebankingapp.data.RetrofitRepository
import com.example.mobilebankingapp.model.ExchangeRateResponse
import com.example.mobilebankingapp.model.GoogleMapsApiResponse
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class HelpViewModel(private val retrofitRepository: RetrofitRepository, private val locationHandler: LocationHandler) : ViewModel() {
    val currentLocation: MutableState<LatLng> = mutableStateOf(LatLng(44.634775, 22.664495))
    val nearbyAtmList: MutableState<List<GoogleMapsApiResponse>> = mutableStateOf(listOf())
    private lateinit var locationCallback: LocationCallback

    init {
        setupLocation()
    }

    private fun setupLocation() {
        this.locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation.let {
                    currentLocation.value = LatLng(it.latitude, it.longitude)
                    retrofitRepository.getNearbyAtms("${currentLocation.value.latitude}, ${currentLocation.value.longitude}") {
                        nearbyAtmList.value = it
                    }
                }
            }
        }
        locationHandler.registerLocationListener(locationCallback)
    }
}