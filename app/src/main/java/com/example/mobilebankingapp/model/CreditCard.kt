package com.example.mobilebankingapp.model

data class CreditCard(
    val cardHolderName: String = "",
    val serialNumber: String = "",
    val expirationDate: String = "",
    val cvv: Int = 0,
    val isDefault: Boolean = false
)
