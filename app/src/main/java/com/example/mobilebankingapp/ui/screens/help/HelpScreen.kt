package com.example.mobilebankingapp.ui.screens.help

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.mobilebankingapp.model.GoogleMapsApiResponse
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import okhttp3.internal.wait


@Composable
fun HelpScreen(
    helpViewModel: HelpViewModel
) {
    val ctx = LocalContext.current
    helpViewModel.setupLocation()
    helpViewModel.locationHandler = LocationHandler(ctx)
    Column {
        MapComposable(helpViewModel = helpViewModel)
    }
}
    @Composable
    private fun MapComposable(helpViewModel: HelpViewModel) {
        var atmList = listOf<GoogleMapsApiResponse>()

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(helpViewModel.currentLocation.value, 10f)
        }
        LaunchedEffect(helpViewModel.currentLocation.value) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(helpViewModel.currentLocation.value, 15f, 0f, 0f)
                ),
                durationMs = 100
            )
            atmList = helpViewModel.getAtmsData(
                helpViewModel.currentLocation.value.latitude.toString(),
                helpViewModel.currentLocation.value.longitude.toString()
            ).value
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            cameraPositionState.let {
                LaunchedEffect(cameraPositionState.position.target.latitude, cameraPositionState.position.target.longitude) {
                    atmList = helpViewModel.getAtmsData(
                        helpViewModel.currentLocation.value.latitude.toString(),
                        helpViewModel.currentLocation.value.longitude.toString()
                    ).value
                }
//                atmList.value = helpViewModel.getAtmsData(cameraPositionState.position.target.latitude.toString(), cameraPositionState.position.target.longitude.toString()).value
                atmList.forEach{
                    val lat = it.location.first()["lat"] ?: 0.0
                    val lng = it.location.first()["lng"] ?: 0.0
                    val position = LatLng(lat, lng)
                    Marker(
                        state = MarkerState(position = position),
                        title = it.name,
                        snippet = it.name
                    )
                }
            }
        }
    }

//@Composable
//fun HelpScreen(
//    helpViewModel: HelpViewModel
//) {
//    val ctx = LocalContext.current
//    helpViewModel.setupLocation()
//    helpViewModel.locationHandler = LocationHandler(ctx)
//    val initialLocation = helpViewModel.initLocation()
//
//    Column {
//        MapComposable(helpViewModel = helpViewModel, initialLocation = initialLocation)
//    }
//}

//@Composable
//private fun MapComposable(
//    helpViewModel: HelpViewModel,
//    initialLocation: LatLng
//) {
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
//    }
//
//    LaunchedEffect(helpViewModel.currentLocation.value) {
//        cameraPositionState.animate(
//            update = CameraUpdateFactory.newCameraPosition(
//                CameraPosition(initialLocation, 15f, 0f, 0f)
//            ),
//            durationMs = 100
//        )
//    }
//
//    LaunchedEffect(helpViewModel.nearbyAtmList.value) {
//        // Update the map when nearbyAtmList changes
//    }
//
//    GoogleMap(
//        modifier = Modifier.fillMaxSize(),
//        cameraPositionState = cameraPositionState,
//    ) {
//        val nearbyAtmsData = helpViewModel.nearbyAtmList.value
//        nearbyAtmsData.forEach { atm ->
//            val lat = atm.location.first()["lat"] ?: 0.0
//            val lng = atm.location.first()["lng"] ?: 0.0
//            val position = LatLng(lat, lng)
//            Marker(
//                state = MarkerState(position = position),
//                title = atm.name,
//                snippet = atm.name
//            )
//        }
//    }
//}

fun getGoogleMapsApiKey(context: Context): String {
    try {
        val applicationInfo = context.packageManager.getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        )
        val bundle: Bundle? = applicationInfo.metaData
        return bundle?.getString("com.google.android.geo.API_KEY", "") ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}