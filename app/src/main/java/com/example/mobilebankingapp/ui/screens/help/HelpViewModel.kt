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

class HelpViewModel (private val retrofitRepository: RetrofitRepository) : ViewModel() {
    val currentLocation: MutableState<LatLng> = mutableStateOf(LatLng(44.634775, 22.664495))
    var locationHandler: LocationHandler? = null
    private var locationCallback: LocationCallback? = null

    val nearbyAtmList: MutableState<MutableList<GoogleMapsApiResponse>> =
        try {
            retrofitRepository.getNearbyAtms("${currentLocation.value.latitude},${currentLocation.value.longitude}")
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

    fun setupLocation() {
        this.locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation.let {
//                    try {
//                        nearbyAtmList.value = retrofitRepository.getNearbyAtms("${it.latitude},${it.longitude}").value
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        throw e
//                    }
                    currentLocation.value = LatLng(it.latitude, it.longitude)
                }
            }
        }
        locationHandler?.registerLocationListener(locationCallback!!)
    }

    fun getAtmsData(
        latitude: String,
        longitude: String
    ): MutableState<MutableList<GoogleMapsApiResponse>> {
        try {
            return retrofitRepository.getNearbyAtms("$latitude, $longitude")
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

//    fun updateLocation(latitude: String, longitude: String) {
//        nearbyAtmList.value =
//            try {
//                retrofitRepository.getNearbyAtms("${latitude},${longitude}").value
//            } catch (e: Exception) {
//                e.printStackTrace()
//                throw e
//            }
//    }
}