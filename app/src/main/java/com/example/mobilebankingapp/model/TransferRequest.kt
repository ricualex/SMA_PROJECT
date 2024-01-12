package com.example.mobilebankingapp.model

data class TransferRequest(
    val userFrom: String,
    val userTo: String,
    val amount: Int
)
