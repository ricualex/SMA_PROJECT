package com.example.mobilebankingapp

import android.app.Application
import com.example.mobilebankingapp.data.AppContainer
import com.example.mobilebankingapp.data.DefaultAppContainer

class BankingApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}