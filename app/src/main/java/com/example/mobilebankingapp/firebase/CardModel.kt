package com.example.mobilebankingapp.firebase

data class CardModel(
    val cardHolderName: String,
    val serialNumber: String,
    val expirationDate: String,
    val cvv: Int
) {
    constructor() : this("", "", "", 0)
}
