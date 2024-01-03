package com.example.mobilebankingapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobilebankingapp.ui.screens.home.HomeViewModel
import com.example.mobilebankingapp.ui.screens.signin.SignInViewModel

object ViewModelProvider {

    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val repo = getApplication().container.googleAuthRepository
            SignInViewModel(repo)
        }

        initializer {
            val repo = getApplication().container.firebaseRepository
            HomeViewModel(repo)
        }
    }

    private fun CreationExtras.getApplication() =
        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BankingApplication
}