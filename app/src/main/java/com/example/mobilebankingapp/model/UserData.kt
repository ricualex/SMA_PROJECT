package com.example.mobilebankingapp.model

data class UserData(
    val firstName: String? = "",
    val lastName: String? = "",
    val birthDate: String? = "",
    val cnp: String? = "",
    val balance: Map<String, Double> = mapOf("RON" to 0.0),
    val cards: Map<String, CreditCard> = mapOf()
)
