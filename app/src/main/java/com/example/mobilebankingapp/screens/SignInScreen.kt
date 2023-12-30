package com.example.mobilebankingapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilebankingapp.R
import com.example.mobilebankingapp.components.BoldTextComponent
import com.example.mobilebankingapp.components.DefaultBlueButton
import com.example.mobilebankingapp.components.NormalTextComponent
import com.example.mobilebankingapp.presentation.login.SignInState

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Column {
        val logoPainter = painterResource(id = R.drawable.bank_logo)
        BoldTextComponent("Mobile Banking App")
        NormalTextComponent("Hello there,")
        NormalTextComponent("Welcome back")
        Spacer(modifier = Modifier
            .heightIn(40.dp)
        )
        Box(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            DefaultBlueButton(value = "Sign In With Google", onSignInClick = onSignInClick)
        }
        Image(
            painter = logoPainter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}