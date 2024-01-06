package com.example.mobilebankingapp.data

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity

interface AppContainer {
    val googleAuthRepository: GoogleAuthRepository
    val firebaseRepository: FirebaseRepository
    val retrofitRepository : RetrofitRepository
}

class DefaultAppContainer(ctx: Context) : AppContainer {
    private val serverClientId =
        "634380083220-jhg190uunuj5no2uequ8b2k62n4jdrdt.apps.googleusercontent.com"
    override val googleAuthRepository by lazy {
        NetworkGoogleAuthRepository(serverClientId, Identity.getSignInClient(ctx))
    }

    override val firebaseRepository: FirebaseRepository by lazy {
        NetworkFirebaseRepository()
    }

    override val retrofitRepository: RetrofitRepository by lazy {
        NetworkRetrofitRepository()
    }
}