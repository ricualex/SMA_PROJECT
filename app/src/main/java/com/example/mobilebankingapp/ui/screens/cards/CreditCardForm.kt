package com.example.mobilebankingapp.ui.screens.cards

import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilebankingapp.R
import com.example.mobilebankingapp.ViewModelProvider
import com.example.mobilebankingapp.utils.biometricAuth
import com.example.mobilebankingapp.ui.theme.SpaceMono

@Composable
fun CreditCardForm(
    modifier: Modifier = Modifier,
    viewModel: CreditCardViewModel = viewModel(factory = ViewModelProvider.Factory),
    readOnly: Boolean = true
) {
    val creditCard = viewModel.creditCard.value
    val isDisplayed = viewModel.isDisplayed.value
    Card(
        modifier = modifier
            .height(200.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.card_mesh),
                contentDescription = "Card Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Image(
                painter = painterResource(id = creditCard.issuer.issuerIcon),
                contentDescription = "Card Issuer",
                modifier = Modifier
                    .padding(16.dp)
                    .height(68.dp)
                    .width(86.dp)
                    .align(Alignment.TopStart)
            )
            if (readOnly) {
                val ctx = LocalContext.current
                IconButton(
                    onClick = {
                        if (!viewModel.identityConfirmed) {
                            biometricAuth(ctx) { viewModel.identityConfirmed = it }
                        }
                        viewModel.toggleDisplay()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = "display info")
                }
            }
            val isMasked = isDisplayed || !readOnly
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                CreditCardTextField(
                    value = creditCard.panNumber,
                    readOnly = readOnly,
                    isDisplayed = isMasked,
                    placeholder = "Credit card number",
                    isError = !viewModel.isPanNumberValid(),
                    onValueChange = viewModel::updatePanNumber,
                    unmaskedChars = 4
                )
                CreditCardTextField(
                    value = creditCard.holderName,
                    readOnly = readOnly,
                    placeholder = "Holder name",
                    isDisplayed = isMasked,
                    onValueChange = viewModel::updateHolderName,
                    keyboardType = KeyboardType.Text
                )
            }
            CreditCardTextField(
                value = creditCard.cvv,
                readOnly = readOnly,
                placeholder = "CVV",
                isDisplayed = isMasked,
                onValueChange = viewModel::updateCvv,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterEnd),
                isError = !viewModel.isCvvValid()
            )

            CreditCardTextField(
                value = creditCard.expirationDate,
                readOnly = readOnly,
                placeholder = "Exp date",
                isDisplayed = true,
                onValueChange = viewModel::updateExpirationDate,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                visualTransformation = ExpiryDateTransformation(),
                isError = !viewModel.isExpirationDateValid()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardTextField(
    value: String,
    readOnly: Boolean,
    isDisplayed: Boolean,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    unmaskedChars: Int = 0,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified,
            fontFamily = SpaceMono,
            letterSpacing = 1.2.sp,
            fontSize = 16.sp,
            textAlign = TextAlign.End
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        interactionSource = interactionSource,
        readOnly = readOnly,
        visualTransformation = if (isDisplayed)
            visualTransformation
        else
            CardInfoMaskTransformation(unmaskedChars),
        modifier = modifier
    ) {
        TextFieldDefaults.TextFieldDecorationBox(
            value = value,
            placeholder = {
                Text(
                    color = Color.Unspecified,
                    text = placeholder,
                    fontFamily = SpaceMono,
                    letterSpacing = 1.2.sp,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End,
                )
            },
            innerTextField = it,
            enabled = true,
            singleLine = true,
            isError = isError,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(4.dp),
        ) {
        }
    }
}

class CardInfoMaskTransformation(private val unmaskedChars: Int = 0) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val str = text.text
        if (str.length < unmaskedChars) {
            return TransformedText(AnnotatedString(str), OffsetMapping.Identity)
        }
        val replaceLen = str.length - unmaskedChars
        val newText = str.replaceRange(0, replaceLen, "*".repeat(replaceLen))
        return TransformedText(AnnotatedString(newText), OffsetMapping.Identity)
    }
}

class ExpiryDateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(4)
        val formatted = trimmed.mapIndexed { i, c ->
            if (i == 1) {
                "$c/"
            } else {
                c
            }
        }.joinToString(separator = "")

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    in 0..1 -> offset
                    else -> offset + 1
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    in 0..1 -> offset
                    else -> offset - 1
                }
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetTranslator)
    }
}
