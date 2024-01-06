package com.example.mobilebankingapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilebankingapp.R
import com.example.mobilebankingapp.components.AppFonts
import com.example.mobilebankingapp.components.RoundGreyButton
import com.example.mobilebankingapp.model.ExchangeRateResponse
import com.example.mobilebankingapp.model.UserData
import com.example.mobilebankingapp.model.UserProfile
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.core.text.isDigitsOnly

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    dataModel: UserData,
    exchangeData: ExchangeRateResponse
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    println(exchangeData)
                })
            }
            ApiBox(apiExchangeRateData = exchangeData)
        }
    }
}

@Composable
fun WelcomeBox(userProfile: UserProfile, firebaseDataState: UserData) {
    Column {
        Card(
            modifier = Modifier
                .size(200.dp),
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Welcome, ${userProfile.username?.split(" ")?.first()}",
                        style = AppFonts.TitleFontStyle
                    )
                    firebaseDataState.balance.entries.firstOrNull()?.let {
                        Text(
                            text = "Current balance:",
                            style = AppFonts.TitleFontStyle
                        )
                        Text(
                            text = "${it.value} ${it.key}",
                            style = AppFonts.TitleFontStyle
                        )
                    }
                }
            }
        )
    }
}
@Composable
fun ApiBox(apiExchangeRateData: ExchangeRateResponse) {
    var currencyFrom by remember { mutableStateOf("10") }
    var currencyTo by remember { mutableStateOf("10") }
    val usdPainter = painterResource(id = R.drawable.usd)
    val euroPainter = painterResource(id = R.drawable.euro)

    CurrencyRow(
        currency = "EUR",
        data = apiExchangeRateData,
        imagePainter = euroPainter,
        color = Color.Green,
        text = currencyFrom,
        onValueChange = {
            try {
                val convertedValue = it.toDouble() * apiExchangeRateData.rates["USD"]!!
                currencyFrom = it
                currencyTo = convertedValue.toString()
            }
            catch (e: Exception) {
                e.printStackTrace()
                currencyTo = "0"
                currencyFrom = "0"
            }
        }
    )

    CurrencyRow(
        currency = "USD",
        data = apiExchangeRateData,
        imagePainter = usdPainter,
        color = Color.Red,
        text = currencyTo,
        onValueChange = {
            try {
                val convertedValue = it.toDouble() / apiExchangeRateData.rates["USD"]!!
                currencyTo = it
                currencyFrom = convertedValue.toString()
            }
            catch (e: Exception) {
                e.printStackTrace()
                currencyTo = "0"
                currencyFrom = "0"
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiTextField(
    currency: String,
    color: Color,
    text: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = {
            if (it.isDigitsOnly()) {
                onValueChange(it)
            }
        },
        label = { Text(text = currency, style = TextStyle(fontSize = 30.sp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 30.sp, color = color),
        singleLine = true
    )
}

@Composable
fun CurrencyRow(
    currency: String,
    data: ExchangeRateResponse,
    imagePainter: Painter,
    color: Color,
    text: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .padding(16.dp)
                .width(70.dp)
                .height(70.dp)
        )

        ApiTextField(currency, color, text) {
            onValueChange(it)
        }
    }
}
