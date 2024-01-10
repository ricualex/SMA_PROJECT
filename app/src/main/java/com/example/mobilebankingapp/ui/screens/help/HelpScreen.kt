package com.example.mobilebankingapp.ui.screens.help


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun HelpScreen(
    helpViewModel: HelpViewModel
) {
    Column {
        MapComposable(helpViewModel = helpViewModel)
    }
}

@Composable
private fun MapComposable(helpViewModel: HelpViewModel) {
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
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
    ) {
        cameraPositionState.let {
            LaunchedEffect(
                cameraPositionState.position.target.latitude,
                cameraPositionState.position.target.longitude
            ) {
            }
            helpViewModel.nearbyAtmList.value.forEach {
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