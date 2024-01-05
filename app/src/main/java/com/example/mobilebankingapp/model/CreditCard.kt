package com.example.mobilebankingapp.model

import androidx.annotation.DrawableRes
import com.example.mobilebankingapp.R

enum class CardIssuer(@DrawableRes val issuerIcon: Int) {
    MasterCard(R.drawable.mc),
    Visa(R.drawable.visa),
    Unknown(R.drawable.default_icon)
}

data class CreditCard(
    val holderName: String = "",
    val panNumber: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val issuer: CardIssuer = CardIssuer.Unknown,
    val default: Boolean = false
)
