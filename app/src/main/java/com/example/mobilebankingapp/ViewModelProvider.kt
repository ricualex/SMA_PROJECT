package com.example.mobilebankingapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobilebankingapp.ui.screens.cards.CreditCardViewModel
import com.example.mobilebankingapp.ui.screens.help.HelpViewModel
import com.example.mobilebankingapp.ui.screens.help.LocationHandler
import com.example.mobilebankingapp.ui.screens.home.ApiViewModel
import com.example.mobilebankingapp.ui.screens.home.UserViewModel
import com.example.mobilebankingapp.ui.screens.signin.SignInViewModel
import com.example.mobilebankingapp.ui.screens.transfer.TransferViewModel

object ViewModelProvider {

    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val repo = getApplication().container.googleAuthRepository
            SignInViewModel(repo)
        }

        initializer {
            val repo = getApplication().container.firebaseRepository
            UserViewModel(repo)
        }

        initializer {
            CreditCardViewModel()
        }

        initializer {
            val retrofitRepo = getApplication().container.retrofitRepository
            val firebaseRepo = getApplication().container.firebaseRepository
            ApiViewModel(retrofitRepo, firebaseRepo)
        }
        initializer {
            val app = getApplication()
            val retrofitRepo = app.container.retrofitRepository
            val locationHandler = LocationHandler(app.applicationContext)
            HelpViewModel(retrofitRepo, locationHandler)
        }
        initializer {
            val retrofitRepo = getApplication().container.retrofitRepository
            val firebaseRepo = getApplication().container.firebaseRepository
            TransferViewModel(retrofitRepo, firebaseRepo)
        }
    }

    private fun CreationExtras.getApplication() =
        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BankingApplication
}