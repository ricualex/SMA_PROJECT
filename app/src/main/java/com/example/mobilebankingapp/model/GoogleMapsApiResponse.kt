package com.example.mobilebankingapp.model

data class GoogleMapsApiResponse(
    val name: String = "",
    val location: List<Map<String, Double>> = listOf()
)
