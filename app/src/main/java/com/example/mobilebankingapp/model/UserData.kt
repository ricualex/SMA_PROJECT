package com.example.mobilebankingapp.model

data class UserData(
    // TODO: change firebase model
//    val username: String = "",
//    val firstName: String = "",
//    val lastName: String = "",
//    val dateOfBirth: String = "",
//    val cnp: String = "",
//    val age: String = "",
    val balance: Map<String, Int> = mapOf(),
    val cards: Map<String, CreditCard> = mapOf()
)
