package com.example.mobilebankingapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultBlueButton(value: String, onSignInClick: () -> Unit) {
    Button(onClick = onSignInClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0, 0, 139),
            contentColor = Color(0, 0, 139),
        )
    ) {
        Text(
            text = value,
            style = TextStyle(
                color = Color.White
            )
        )
    }
}

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp)
            .heightIn(min = 10.dp),
        style = TextStyle(
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Normal
        ),
    )
}
@Composable
fun BoldTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .heightIn(min = 80.dp),
        style = TextStyle(
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center
    )
}