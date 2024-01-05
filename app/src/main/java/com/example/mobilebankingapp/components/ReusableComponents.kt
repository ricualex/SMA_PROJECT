package com.example.mobilebankingapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
fun DefaultBlueButton(value: String, onButtonClick: () -> Unit) {
    Button(
        onClick = onButtonClick,
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
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RoundBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun RoundGreyButton(value: String, onButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onButtonClick,
    ) {
        Text(
            text = value,
            style = AppFonts.NormalButtonFontStyle
        )
    }
}