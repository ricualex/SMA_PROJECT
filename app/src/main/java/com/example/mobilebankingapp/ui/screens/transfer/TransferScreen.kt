package com.example.mobilebankingapp.ui.screens.transfer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilebankingapp.components.RoundGreyButton
import com.example.mobilebankingapp.model.TransferRequest

@Composable
fun TransferScreen(
    transferViewModel: TransferViewModel
) {
    val ctx = LocalContext.current
    var transferTo by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Please provide the first name of the person you want to transfer money to:")
        Spacer(modifier = Modifier.height(20.dp))
        TransferTextField(
            labelText = "To",
            text = transferTo
        ) {
            transferTo = it
        }
        Spacer(modifier = Modifier.height(30.dp))
        AmountTextField(
            labelText = "Amount",
            text = amount
        ) {
            amount = it
        }
        Spacer(modifier = Modifier.height(30.dp))
        RoundGreyButton(value = "Confirm", onButtonClick = {
            val result = transferViewModel.doTransfer(transferTo, amount.toInt())
            if (result == 1) {
                Toast.makeText(ctx, "Transfer successful!", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(ctx, "Transfer failed!", Toast.LENGTH_SHORT).show()
            }
        })

    }
}

@Composable
fun TransferTextField(
    labelText: String,
    text: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = labelText, style = TextStyle(fontSize = 20.sp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
        singleLine = true
    )
}

@Composable
fun AmountTextField(
    labelText: String,
    text: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(text = labelText, style = TextStyle(fontSize = 20.sp)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = TextStyle(fontSize = 20.sp, color = Color.White),
        singleLine = true
    )
}