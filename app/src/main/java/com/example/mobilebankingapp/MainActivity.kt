package com.example.mobilebankingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilebankingapp.firebase.FirebaseDbStore
import com.example.mobilebankingapp.presentation.homepage.HomeViewModel
import com.example.mobilebankingapp.presentation.login.GoogleAuthClient
import com.example.mobilebankingapp.presentation.login.SignInViewModel
import com.example.mobilebankingapp.screens.HomeScreen
import com.example.mobilebankingapp.screens.SignInScreen
import com.example.mobilebankingapp.ui.theme.MobileBankingAppTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContent {
            MobileBankingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController , startDestination = "login") {
                        composable("login") {
                            val viewModel = viewModel<SignInViewModel>()

                            val state by viewModel.state.collectAsState()
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccesful) {
                                if(state.isSignInSuccesful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed In successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate("homepage")
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                        }
                        composable("homepage") {
                            val userId = googleAuthUiClient.getSignedInUser()?.userId
                            if (userId != null) {
                                val viewModel = HomeViewModel(FirebaseDbStore(userId = userId))
                                viewModel.googleAuthUiClient = googleAuthUiClient
                                viewModel.userData = googleAuthUiClient.getSignedInUser()!!
                                HomeScreen(
                                    userData = viewModel.userData,
                                    firebaseDataFlow = viewModel.userState,
                                    onTransferClick = {viewModel.onTransferClick()},
                                    onAddOrChangeCard = {viewModel.onAddOrChangeCard()},
                                    onBuyDifferentCurrency = {viewModel.onBuyDifferentCurrency()},
                                    onHelpClick = {viewModel.onHelpClick()},
                                    onLogout = {logOut(googleAuthUiClient, navController)}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    private fun logOut(googleAuthClient: GoogleAuthClient, navController: NavController) {
        lifecycleScope.launch {
            googleAuthUiClient.signOut()
        }
        navigateToScreen(navController, "login")
        Toast.makeText(
            applicationContext,
            "Signed Out successful",
            Toast.LENGTH_LONG
        ).show()
    }
    private fun navigateToScreen (navController: NavController, screen: String) {
        navController.navigate(screen)
    }
}