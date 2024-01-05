package com.example.mobilebankingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilebankingapp.ui.screens.BankingApp
import com.example.mobilebankingapp.ui.screens.signin.SignInViewModel
import com.example.mobilebankingapp.ui.screens.signin.SingInScreen
import com.example.mobilebankingapp.ui.theme.MobileBankingAppTheme
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch

class MainActivity :  FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContent {
            MobileBankingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val viewModel: SignInViewModel = viewModel(factory = ViewModelProvider.Factory)
                    val coroutineScope = rememberCoroutineScope();
                    val signInState = viewModel.state.collectAsState()

                    if (signInState.value.isSignInSuccesful) {
                        viewModel.googleAuthRepository.getSignedInUser()?.userId?.let { userId ->
                            BankingApp(
                                activity = this,
                                userProfile = viewModel.getSignedInUser()!!,
                                userId = userId,
                                onLogOutClicked = {
                                    coroutineScope.launch {
                                        viewModel.signOut()
                                    }
                                    viewModel.resetState()
                                    Toast.makeText(
                                        applicationContext,
                                        "Signed Out successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        } ?: Text(text = "Not able to retrieve user id") // TODO: make this pretty
                    } else {
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = {
                                if (it.resultCode == RESULT_OK) {
                                    coroutineScope.launch {
                                        val signInResult = viewModel.signInWithIntent(
                                            intent = it.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = signInState.value.isSignInSuccesful) {
                            if (signInState.value.isSignInSuccesful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Signed In successful",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        SingInScreen(
                            state = signInState.value,
                            onSignInClicked = {
                                coroutineScope.launch {
                                    val signInIntentSender =
                                        viewModel.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}