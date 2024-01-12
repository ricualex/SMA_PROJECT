package com.example.mobilebankingapp.model

data class UserProfile(
    val userId: String,
    val username: String? = "",
    val profilePictureUrl: String? = ""
)