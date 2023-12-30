package com.example.mobilebankingapp.presentation.login

data class SignInState(
    val isSignInSuccesful: Boolean = false,
    val signInError: String? = null
)
