package com.example.mobilebankingapp.utils

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity


fun biometricAuth(context: Context, authCallBack: (Boolean) -> Unit) {
    if (context !is FragmentActivity) {
        authCallBack(false)
        return
    }
    val biometricPrompt = BiometricPrompt(context,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                authCallBack(false)
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                authCallBack(true)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    context, "Authentication failed",
                    Toast.LENGTH_SHORT
                ).show()
                authCallBack(false)
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Verify your identity")
        .setSubtitle("")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(promptInfo)
}