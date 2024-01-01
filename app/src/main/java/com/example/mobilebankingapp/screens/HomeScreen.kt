package com.example.mobilebankingapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobilebankingapp.components.AppBar
import com.example.mobilebankingapp.components.AppColors
import com.example.mobilebankingapp.components.AppFonts
import com.example.mobilebankingapp.components.MenuItem
import com.example.mobilebankingapp.components.RoundBox
import com.example.mobilebankingapp.components.RoundGreyButton
import com.example.mobilebankingapp.firebase.UserDataModel
import com.example.mobilebankingapp.presentation.login.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userData: UserData?,
    firebaseDataFlow: Flow<UserDataModel>,
    onLogout: () -> Unit,
    onTransferClick: () -> Unit,
    onAddOrChangeCard: () -> Unit,
    onBuyDifferentCurrency: () -> Unit,
    onHelpClick: () -> Unit,
 ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.PrimaryBackground)
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val firebaseDataState = firebaseDataFlow.collectAsState(UserDataModel())

        ModalNavigationDrawer(
            modifier = Modifier.background(AppColors.PrimaryBackground),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerHeader(userData)
                    DrawerBody(
                        items = listOf(
                            MenuItem(
                                id = "transfer",
                                title = "Transfer",
                                contentDescription = "Go To Transfer",
                                icon = Icons.Default.Send,
                                onClick = onTransferClick
                            ),
                            MenuItem(
                                id = "cards",
                                title = "Cards",
                                contentDescription = "Go To Cards",
                                icon = Icons.Default.AccountBox,
                                onClick = onAddOrChangeCard
                            ),
                            MenuItem(
                                id = "currency",
                                title = "Money Exchange",
                                contentDescription = "Buy/Sell currency",
                                icon = Icons.Default.Refresh,
                                onClick = onBuyDifferentCurrency
                            ),
                            MenuItem(
                                id = "help",
                                title = "Help",
                                contentDescription = "Go To Help",
                                icon = Icons.Default.Info,
                                onClick = onHelpClick
                            ),
                            MenuItem(
                                id = "logout",
                                title = "Logout",
                                contentDescription = "Logout",
                                icon = Icons.Default.ExitToApp,
                                onClick = onLogout
                            )
                        ),
                        onItemClick = {
                            it.onClick()
                        }
                    )
                }
            },
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppBar(
                    onNavigationIconClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
                WelcomeBox(userData, firebaseDataState.value)
                Spacer(modifier = Modifier.padding(top = 20.dp))
                Box(modifier = Modifier
                ) {
                    RoundGreyButton(value  = "Make a payment", onButtonClick = {
                        println(firebaseDataState.value.username)
                    })
                }
            }
        }
    }
}

@Composable
fun DrawerHeader(userData: UserData?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.PrimaryBackground),
    ) {
        Column (

        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                AsyncImage(model = userData?.profilePictureUrl, contentDescription = null)
                Text(text = "${userData?.username}", style = AppFonts.TitleFontStyle, modifier = Modifier.padding(start = 10.dp))
            }
            Text(text = "Menu", style = AppFonts.MenuHeaderFontStyle, modifier = Modifier.padding(start = 10.dp))
        }
    }
}
@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier
        .background(AppColors.PrimaryBackground)
        .fillMaxSize(),
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp, color = AppColors.PrimaryFontColor),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier) {
        items(items) { item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick(item)
                }
                .padding(16.dp)
            ) {
                Icon(imageVector = item.icon, contentDescription = null, tint = AppColors.PrimaryFontColor)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}

@Composable
fun WelcomeBox(userData: UserData?, firebaseDataState: UserDataModel?) {
    Column {
        RoundBox(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp),
            content = {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome, ${userData?.username?.split(" ")?.firstOrNull()}",
                        style = AppFonts.TitleFontStyle
                    )
                    if (firebaseDataState?.balance?.entries?.firstOrNull() != null) {
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