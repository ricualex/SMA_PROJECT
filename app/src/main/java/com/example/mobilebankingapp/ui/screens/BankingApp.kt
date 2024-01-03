package com.example.mobilebankingapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobilebankingapp.components.AppBar
import com.example.mobilebankingapp.model.UserData
import com.example.mobilebankingapp.model.UserProfile
import com.example.mobilebankingapp.ui.screens.cards.CardsScreen
import com.example.mobilebankingapp.ui.screens.drawer.DrawerScreen
import com.example.mobilebankingapp.ui.screens.home.HomeScreen
import kotlinx.coroutines.launch


enum class BankingAppScreen(val title: String) {
    Home("Home"),
    Cards("Credit Cards")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankingApp(userProfile: UserProfile, dataModel: UserData, onLogOutClicked: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val crtScreen = BankingAppScreen.valueOf(
        backStackEntry?.destination?.route ?: BankingAppScreen.Home.name
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(crtScreen = crtScreen) {
                coroutineScope.launch {
                    if (drawerState.isOpen) {
                        drawerState.close()
                    } else {
                        drawerState.open();
                    }
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            DrawerScreen(
                drawerState = drawerState,
                userProfile = userProfile,
                onCardsClicked = {
                    navController.navigate(BankingAppScreen.Cards.name)
                    // TODO: this can be done better
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
                onLogOutClicked = onLogOutClicked
            ) {
                NavHost(
                    navController = navController,
                    startDestination = BankingAppScreen.Home.name
                ) {
                    composable(route = BankingAppScreen.Home.name) {
                        HomeScreen(userProfile = userProfile, dataModel = dataModel)
                    }
                    composable(route = BankingAppScreen.Cards.name) {
                        CardsScreen()
                    }
                }
            }
        }
    }
}