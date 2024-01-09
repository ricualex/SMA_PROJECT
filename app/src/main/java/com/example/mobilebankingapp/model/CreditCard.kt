package com.example.mobilebankingapp.model

import androidx.annotation.DrawableRes
import com.example.mobilebankingapp.R
import com.example.mobilebankingapp.utils.decryptData
import com.example.mobilebankingapp.utils.encryptData
import com.example.mobilebankingapp.utils.getSecretKey

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
) {

    fun encrypt(): CreditCard {
        val key = getSecretKey("test")
        return copy(
            holderName = encryptData(holderName, key),
            panNumber = encryptData(panNumber, key),
            cvv = encryptData(cvv, key)
        )
    }

    fun decrypt(): CreditCard {
        val key = getSecretKey("test")
        return copy(
            holderName = decryptData(holderName, key),
            panNumber = decryptData(panNumber, key),
            cvv = decryptData(cvv, key)
        )
    }
}
