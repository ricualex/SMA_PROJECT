package com.example.mobilebankingapp.firebase

data class UserDataModel(
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val cnp: String = "",
    val age: String = "",
    val balance: Map<String, Int> = mapOf(),
    val cards: List<CardModel> = emptyList()
)
