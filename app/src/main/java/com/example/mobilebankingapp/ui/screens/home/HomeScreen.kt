package com.example.mobilebankingapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilebankingapp.components.AppColors
import com.example.mobilebankingapp.components.AppFonts
import com.example.mobilebankingapp.components.RoundBox
import com.example.mobilebankingapp.components.RoundGreyButton
import com.example.mobilebankingapp.model.UserData
import com.example.mobilebankingapp.model.UserProfile

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    dataModel: UserData,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.PrimaryBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeBox(userProfile, dataModel)
            Spacer(modifier = Modifier.padding(top = 20.dp))
            Box(
                modifier = Modifier
            ) {
                RoundGreyButton(value = "Make a payment", onButtonClick = {
                })
            }
        }
    }
}


@Composable
fun WelcomeBox(userProfile: UserProfile, firebaseDataState: UserData) {
    Column {
        RoundBox(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp),
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        // TODO: this is not good
                        text = "Welcome, ${userProfile.username?.split(" ")?.first()}",
                        style = AppFonts.TitleFontStyle
                    )
                    if (firebaseDataState.balance.entries.firstOrNull() != null) {
                        Text(
                            text = "Current balance:",
                            style = AppFonts.TitleFontStyle
                        )
                        Text(
                            text = "${firebaseDataState.balance.entries.firstOrNull()?.value} ${firebaseDataState.balance.entries.firstOrNull()?.key}",
                            style = AppFonts.TitleFontStyle
                        )
                    }
                }
            }
        )
    }
}