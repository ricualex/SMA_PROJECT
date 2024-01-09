package com.example.mobilebankingapp.ui.screens.drawer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mobilebankingapp.components.AppColors
import com.example.mobilebankingapp.components.AppFonts
import com.example.mobilebankingapp.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScreen(
    drawerState: DrawerState,
    userProfile: UserProfile,
    onCardsClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    onExchangeClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader(userProfile)
                DrawerBody(
                    items = listOf(
                        MenuItem(
                            id = "home",
                            title = "Home",
                            contentDescription = "Go To Home",
                            icon = Icons.Default.Home,
                            onClick = onHomeClicked
                        ),
                        MenuItem(
                            id = "transfer",
                            title = "Transfer",
                            contentDescription = "Go To Transfer",
                            icon = Icons.Default.Send,
                            onClick = {}
                        ),
                        MenuItem(
                            id = "cards",
                            title = "Cards",
                            contentDescription = "Go To Cards",
                            icon = Icons.Default.AccountBox,
                            onClick = onCardsClicked
                        ),
                        MenuItem(
                            id = "currency",
                            title = "Money Exchange",
                            contentDescription = "Buy/Sell currency",
                            icon = Icons.Default.Refresh,
                            onClick = onExchangeClicked
                        ),
                        MenuItem(
                            id = "help",
                            title = "Help",
                            contentDescription = "Go To Help",
                            icon = Icons.Default.Info,
                            onClick = onHelpClicked
                        ),
                        MenuItem(
                            id = "logout",
                            title = "Logout",
                            contentDescription = "Logout",
                            icon = Icons.Default.ExitToApp,
                            onClick = onLogOutClicked
                        )
                    ),
                    onItemClick = {
                        it.onClick()
                    }
                )
            }
        },
        content = content
    )
}

@Composable
fun DrawerHeader(userProfile: UserProfile) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(

        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                AsyncImage(model = userProfile.profilePictureUrl, contentDescription = null)
                if (userProfile.username != null) {
                    Text(
                        text = "${userProfile.username}",
                        style = AppFonts.TitleFontStyle,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
            }
            Text(
                text = "Menu",
                style = AppFonts.MenuHeaderFontStyle,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier
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
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = AppColors.PrimaryFontColor
                )
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
