package com.example.mobilebankingapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.mobilebankingapp.ui.screens.BankingAppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    crtScreen: BankingAppScreen,
    onNavigationIconClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = crtScreen.title)
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer",
                    tint = Color.White
                )
            }
        }
    )
}